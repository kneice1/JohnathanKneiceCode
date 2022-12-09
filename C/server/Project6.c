/*******************************
 *
 *  Project Name: Project 6
 *  Description: This program sends files to a  client if it exist. If the client does not exist, 
 * the program will send out a 404 error. This program supports GET and HEAD requests. 
 * In addition, the program allows text, images, and pdf files to be sent.
 *  Date:  April 22, 2022
 *  Authors: Jacob McIntosh and Johnathan Kneice
 *
 *******************************/

#include <netinet/in.h>
#include <netdb.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>

char* getExtensionMessage(char* extension);

int main(int argc, char* argv[])
{
	//If the number of arguments entered is not 3. 
	if(argc != 3)
		// Print what the entered arguments should look like.
		printf("Usage: %s <port> <path to root>\n", argv[0]);
	else
	{
		//Get the port number.
		unsigned int port = atoi(argv[1]);
		// Get the path to root.
		int dir = chdir(argv[2]);
		//If the port number is less than 1024 or greater than 65535 than print out bad port.
		if(port < 1024 || port > 65535)
			printf("Bad port: %d\n", port);
		//If the directory is not found.
		else if(dir == -1)
			printf("Could not change to directory: %s\n", argv[2]);
		else
		{
			//Set up socket.
			int sockFD = socket(AF_INET, SOCK_STREAM, 0);
			int value = 1;
			setsockopt(sockFD, SOL_SOCKET, SO_REUSEADDR, &value, sizeof(value));

			//Setting up the IP.
			struct sockaddr_in address;
			memset(&address, 0, sizeof(address));
			address.sin_family = AF_INET;
			address.sin_port = htons(port);
			address.sin_addr.s_addr = INADDR_ANY;
			bind(sockFD, (struct sockaddr*)&address, sizeof(address));
			
			listen(sockFD, 1);
			
			//Setting up the socket for connection.
			struct sockaddr_storage otherAddress;
			socklen_t otherSize = sizeof(otherAddress);
			int otherSocket;
			
			//endless loop.
			while(1)
			{
				//Waits for connection.
				otherSocket = accept( sockFD, (struct sockaddr *) &otherAddress, &otherSize);
				printf("Got a client!\n");
				//Get the message.
				char message[8192];
				int bytes = read(otherSocket, message, 8191);
				message[bytes] = '\0';
				
				//Seperate the message into fileName and request.
				char request[100];
				char fileName[1024];
				sscanf(message, "%s%s", request, fileName);
				printf("\t%s %s HTTP/1.0\n", request, fileName);
				
				if(strcmp(request, "GET") == 0 || strcmp(request, "HEAD") == 0 )
				{
					//If the last char is a / than add index.html.
					if(fileName[strlen(fileName)-1] == '/')
						strcat(fileName, "index.html");

					//If it starts with a slash skip the slash.
					if(fileName[0] == '/')
					{
						int size = strlen(fileName);
						for(int i = 0; i < size; i++)
							fileName[i] = fileName[i+1];
					}
					
					struct stat information;
					int result = stat(fileName, &information);
					//If file exists.
					if(result == 0)
					{

						char* initialResponse = "HTTP/1.0 200 OK\r\n";
						write(otherSocket, initialResponse, strlen(initialResponse));
						char* extension;
						extension = strrchr(fileName, '.');
						//Send content type.
						char* message = getExtensionMessage(extension);
						if(message != NULL)
						{
							write(otherSocket, message, strlen(message));
						}
						//Send content length.
						char contentLength[1024];
						long sizeOfFile = (long)information.st_size;
						sprintf(contentLength, "Content-Length: %ld\r\n\r\n", sizeOfFile);
						write(otherSocket, contentLength, strlen(contentLength));
						
						//If request is GET we send the file.
						if(strcmp(request, "GET") == 0)
						{
							int fd = open(fileName, O_RDONLY);
							char* buffer = malloc(sizeOfFile);
							read(fd, buffer, sizeOfFile);
							write(otherSocket, buffer, sizeOfFile);
							close(fd);
							free(buffer);
							printf("Sent file: %s\n\n", fileName);
						}
					}
					//If the file does not exist.
					else
					{
						printf("File not found: %s\n\n", fileName);
						char* message = "HTTP/1.0 404 Not Found\r\n\r\n";
						if(strcmp(request, "HEAD") == 0)
							write(otherSocket, message, strlen(message));
						else
						{
							//Send out 404 HTML.
							char buffer[8192];
							sprintf(buffer, "%s<HTML><HEAD><TITLE>404 Not Found</TITLE></HEAD><BODY><H1>Not Found</H1>" 
							"<P>The requested URL %s was not found on this server.</P></BODY></HTML>", message, fileName);
							write(otherSocket, buffer, strlen(buffer));
						}
						
					}
					
				}
				close(otherSocket);
			}
			
		}
	}
	
	return 0;
}
/* 
   Description:Determine what type of content-type message to send. 
				
   Parameters:	Extension- file type at the end of the file.
				
   Returns:	Full content type message.
*/ 
char* getExtensionMessage(char* extension)
{
	if(strcmp(extension, ".html") == 0)
		return "Content-Type: text/html\r\n";
	else if(strcmp(extension, ".htm") == 0)
		return "Content-Type: text/html\r\n";
	else if(strcmp(extension, ".jpg") == 0)
		return "Content-Type: image/jpeg\r\n";
	else if(strcmp(extension, ".jpeg") == 0)
		return "Content-Type: image/jpeg\r\n";
	else if(strcmp(extension, ".gif") == 0)
		return "Content-Type: image/gif\r\n";
	else if(strcmp(extension, ".png") == 0)
		return "Content-Type: text/png\r\n";
	else if(strcmp(extension, ".txt") == 0)
		return "Content-Type: text/plain\r\n";
	else if(strcmp(extension, ".c") == 0)
		return "Content-Type: text/plain\r\n";
	else if(strcmp(extension, ".h") == 0)
		return "Content-Type: text/plain\r\n";
	else if(strcmp(extension, ".pdf") == 0)
		return "Content-Type: application/pdf\r\n";
	//If file type is not a supported type, return NULL.
	else
		return NULL;
}

