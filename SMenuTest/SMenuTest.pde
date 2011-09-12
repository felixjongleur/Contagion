final String ss="move mouse here ...\nor rightclick somewhere ...";
String s=ss;
int mill;

void setup(){
  size(480,320);
  if (frame!=null) frame.setResizable(true);
  textFont(createFont("Times",24));fill(120);
  setupMainMenu(); // setup main menu
}

void draw(){
  background(255);
  stroke(g.fillColor); 
  if (s.length()>30) { // draw arrow   
    pushMatrix();
      translate(20,20); rotate(PI/4);
      line(15,0,135,0); triangle(0,0,15,-3,15,3);
    popMatrix();
  }
  text(s, (width-textWidth(s))/2,150);
  if ((millis()-mill)>3000) s=ss;
}

void mouseClicked(){
  mill=millis();
  if (mouseButton==RIGHT) popupMenu(); 
}


//-----------------------------------------------------------------------
// Main Menu
//-----------------------------------------------------------------------

void setupMainMenu(){
  SMenu m=new SMenu(this); // create new main menu (at 0,0)
  m.SMSubM("File"); // new submenu 
    m.SMItem("Open",new actn(){public void a(){fOpen();}}); // overwrite act.a() with own proc
    m.SMItem("Close",new actn(){public void a(){s="Close clicked";}}); // or do things right here
    m.SMSep(); // insert separator
    m.SMSubM("Import"); 
      m.SMItem("pdf",new actn(){public void a(){s="pdf clicked";}}); 
      m.SMSubM("Image");
        m.SMItem("bmp",null); // no proc yet
        m.SMItem("jpg",new actn(){public void a(){s="jpg clicked";}});
        m.SMItem("png",new actn(){public void a(){s="png clicked";}});
        m.SMEnd(); // don't forget to terminate submenus with SMEnd() 
      m.SMItem("other",null);
      m.SMEnd();
    m.SMSep();
    m.SMItem("Save as",new actn(){public void a(){fSaveAs();}});
    m.SMEnd();
  m.SMSubM("Edit");
    m.SMItem("Cut",new actn(){public void a(){s="Cut clicked";}});
    m.SMItem("Copy",new actn(){public void a(){s="Copy clicked";}});
    m.SMSubM("Copy as");
      m.SMItem("pdf",null);
      m.SMSubM("Image");
        m.SMItem("bmp",null);
        m.SMItem("jpg",null);
        m.SMItem("png",null);
        m.SMSep();  // Separator or Space
        m.SMItem("RGB",null);
        m.SMItem("YUV",null);
        m.SMEnd();
      m.SMItem("txt",null);
      m.SMEnd();
    m.SMSep();
    m.SMItem("Paste",new actn(){public void a(){s="Paste clicked";}});
    m.SMEnd();
  m.SMSubM("Help");
    m.SMItem("About",new actn(){public void a(){About();}});
    m.SMEnd();
  m.SMEnd(); // end all with SMEnd() 
}

void fOpen(){ // your own code here ...
  s="Open clicked";
}
void fSaveAs(){
  s="Save as clicked";
}
void About(){
  s="Simple Menu for Processing";
}

//-----------------------------------------------------------------------
// Popup Menu
//-----------------------------------------------------------------------

void popupMenu(){
  SMenu p=new SMenu(this,mouseX,mouseY);
  p.SMItem("red",new actn(){public void a(){s="Popup red clicked";fill(#FF0000);}}); 
  p.SMItem("green",new actn(){public void a(){s="Popup green clicked";fill(#00FF00);}});
  p.SMItem("blue",new actn(){public void a(){pPng();}}); // or call proc
  p.SMEnd();
}

void pPng(){ // your code here ...
  s="Popup blue clicked";
  fill(#0000FF);
}

