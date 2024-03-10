
//image class.
function Image(url, alt,caption) {
        this.url = url;
        this.alt = alt;
        this.caption =caption
      }
//make a image array.
var count =0;
let image1 = new Image("images/early.jpg","Image of a dog in the early morning.","This is a close up of my dog's face.");
let image2 = new Image("images/early2.jpg","Image of a dog in the early morning","My dog and my sister's dog in a chair.");
let image3 = new Image("images/morning.jpg","Image of a dog in the morning","My dog sitting in a chair after he ate breakfast.");
let image4 = new Image("images/noon.jpg","Image of a dog at noon","Picture of my dog kicking another dog out of its chair.");
let image5 = new Image("images/afternoon.jpg","Image of a dog in the noon","My dog laying on a bench after he went for his noon walk.");
let image6 = new Image("images/dinner.jpg","Image of a dog during dinner time.","Image of my dog on a walk at twilight.");
let image7 = new Image("images/dinner2.jpg","Dog sitting in a chair.","Image of my dog waiting for someone to feed him.");
let image8 = new Image("images/night.jpg","Image of dog at night","My dog before he goes to bed.");
let images = [image1,image2,image3,image4,image5,image6,image7,image8];
//make a date variable.
var date = new Date();
//make an array of months.
const month = ["January", "February", "March","April","May","June","July","August","September","October","November","December"];
let time = "";
let minutes;
let day = ""+(date.getDay()+1);
//add the ending of the day.
if(date.getDay+1==1){
    day=day+"st";
}
else if(date.getDay()+1==2){
    day=day+"nd";
}
else if(date.getDay()+1==3){
    day=day+"rd";
}
else if(date.getDay()+1>=4&&date.getDay()+1<=9){
    day=day+"th";
}
if(date.getMinutes()<=9){
    minutes ="0" + date.getMinutes();
    
}
else{
    minutes = ""+ date.getMinutes();
}
//make a hour variable and mod it by 12.
let hour= date.getHours()%12;
//if the hour is 0 set it to 12.
if(hour==0){
    hour =12
}


//depending on the time of day change the image and caption.
if(date.getHours()<=6){
    time = "It is "+ hour + 
    ":"+minutes+" on this early morning "+
    month[date.getMonth()] +" "+ (day)+".";
    count =0;
}
else if(date.getHours()>6 && date.getHours()<=11){
    time = " Good morning, it is "+ hour + 
    ":"+minutes+" in the morning on "+
    month[date.getMonth()] +" "+ (day)+".";
    count =2;
}
else if(date.getHours() == 12){
    time = "It is lunch time. It is "+ hour + 
    ":"+minutes+" on "+
    month[date.getMonth()] +" "+ (day)+".";
    count=3;
}
else if(date.getHours()>12 && date.getHours()<15){
    time = "It is "+ hour + 
    ":"+minutes+" in the afternoon on "+
    month[date.getMonth()] +" "+ (day)+".";
    count =4;
}
else if(date.getHours()>15 && date.getHours()<18){
    time = "It is dinner time at "+ date.getHours()%12 + 
    ":"+minutes+" on this wonderful "+
    month[date.getMonth()] +" "+ (day)+".";
    count =6;
}
else{
    time = "It is time to go to bed: "+ hour + 
    ":"+minutes+" at night on "+
    month[date.getMonth()] +" "+ (day)+". Good Night.";
    count =7;
}


//changes the image, description, and caption.
let img = document.getElementById('img');
img.setAttribute("src",images[count].url);
img.setAttribute("alt",images[count].alt);
let description = document.getElementById("description");
description.textContent = images[count].caption;
let caption = document.getElementById("caption");
caption.textContent = time;
count++;




//next image function:
//changes the image and description when used.
function nextImage(){
    img = document.getElementById('img');
    img.setAttribute("src",images[count].url);
    img.setAttribute("alt",images[count].alt);
    description = document.getElementById("description");
    description.textContent = images[count].caption;
    count = (count +1) % 8;

}
//changes the image to a random image.
function randomImage(){
    img = document.getElementById('img');
    let random=Math.floor(Math.random()*8)
    count = random;
    description = document.getElementById("description");
    description.textContent = images[count].caption;
    img.setAttribute("src",images[count].url);
    img.setAttribute("alt",images[count].alt);
    count = (count +1) % 8;
}