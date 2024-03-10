/*
1. Do you think your jQuery code is easier to read than the corresponding (pure) JavaScript code,
or harder? Why?

I do believe that my jQuery code is easier to read. Since many of the lines of code can be cut down into a single line. for example:
 img = document.getElementById('img'); 
 img.setAttribute("src",images[count].url);
  can be turned into $("#img").attr("src",images[count].url);

2. Is your jQuery program shorter than the corresponding (pure) JavaScript program?

My jQuery program is shorter than the pure javascript program because I do not have to get the id by using getElementById.
 I can just give the element a new value. This also helps shorten the html because now I do not have to call the javascript button when clicked.

3. Are your answers to the above questions typical for comparisons of jQuery versus JavaScript in
general? Why or why not?

I beleve that my answers are typical for comparing jQuery and javaScript because jQuery allows for the same working code as 
javascript but with less lines of code.
*/ 


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
//Make a function that makes a string with date and time.

function update(){
    const date = new Date();
        //make an array of months.
        const month = ["January", "February", "March","April","May","June","July","August","September","October","November","December"];
        let time = "";
        let minutes;
        let day = ""+(date.getDate());
        //add the ending of the day.
        if(date.getDate+1==1){
            day=day+"st";
        }
        else if(date.getDate()==2){
            day=day+"nd";
        }
        else if(date.getDate()==3){
            day=day+"rd";
        }
        else{
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
        else if(date.getHours()>12 && date.getHours()<=15){
            time = "It is "+ hour + 
            ":"+minutes+" in the afternoon on "+
            month[date.getMonth()] +" "+ (day)+".";
            count =4;
        }
        else if(date.getHours()>15 && date.getHours()<=18){
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
        return time;
}
var time=update()
$("#description").text(time);
$("#img").attr("src",images[count].url);
$("#img").attr("alt",images[count].alt);
$("#caption").text(images[count].caption);
count++

$(document).ready(function(){

    setInterval(function(){
        time=update()
        $("#description").text(time);
        $("#img").attr("src",images[count].url);
        $("#img").attr("alt",images[count].alt);
        $("#caption").text(images[count].caption);
        count++;
    },60000);

    $("#next").click(function(){
        $("#img").attr("src",images[count].url);
        $("#img").attr("alt",images[count].alt);
        $("#caption").text(images[count].caption);
        count = (count +1) % 8;
      });
      $("#random").click(function(){
        let random=Math.floor(Math.random()*8)
        count = random;
        $("#img").attr("src",images[count].url);
        $("#img").attr("alt",images[count].alt);
        $("#caption").text(images[count].caption);
        count = (count +1) % 8;
      });
  });

//next image function:
//changes the image and description when used.

//changes the image to a random image.