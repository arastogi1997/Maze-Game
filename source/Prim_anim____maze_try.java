import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import java.util.HashSet; 
import java.util.LinkedList; 
import java.util.PriorityQueue; 
import java.util.Queue; 
import java.util.Stack; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Prim_anim____maze_try extends PApplet {

//PRIM's Algorithm for finding Minimum Spanning Tree
//Maze generation properly WORKING..!
//Ball movement by keys in newly constructed Weighted Graph from MST, finally working..!!

//Maze GAME made, with DIJKSTRA's Algorithm for displaying solution instantaneously from current vertex.
//fast timer + penalty for using Solution, added point system. Time left / points awarded depend on Dijkstra's path length.

int frR = 100;






WeightedGraph g;
int no = 875;
WeightedGraph mstG;
Vertex end;
ball b;
Timer timer;
ArrayList<Edge> mst = new ArrayList<Edge>();
PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
int points = 0;
public void setup(){
  
  frameRate(frR);
  background(51);
  rectMode(CENTER);
  
  g = new WeightedGraph(true);
  
  for(int i = 0 ; i < no ; i++){
    g.addVertex(i);
  }
  for(int i = 0 ; i < no-26 ; i++ ){
    for (int j = 0; j < 10; j++) {
      //g.addEdge(i, (int)(Math.random()*no),(int)(Math.random()*no)+1);
      if(i%25!=24 && Math.random() < 1.25f)
        g.addEdge(i, abs((i+1)) , 1);
      if( Math.random() < 1.25f )
        g.addEdge(i, (i + 25), 1);
    }
  }
  
  for(int i = no - 25; i < no-1; i++){
    if( Math.random() < 1.4f)g.addEdge(i, abs((i+1)) , 1);
  }
    
  translate(50,50);
  

  
  
  
  //g.printList();
  g.unvisit();
  
  
  mst = new ArrayList<Edge>();
    pq = new PriorityQueue<Edge>();
    g.unvisit();
    visit(g.edgeL.get(0));
  
  mstG = new WeightedGraph(false);
  
  for(int i = 0 ; i < no ; i++){
    mstG.addVertex(i);
  }
  
  b = new ball(g.edgeL.get(0));
  end = g.getVertex(3);
}

public void visit(Vertex v){
    
    v.visited=true;
    for(Edge e : v.edges){  
                                        //here this was wrong, Sedgewick adds both sided edges to edgelist of a a vertex and calls "OTHER".
      if(!e.other(v).visited){          //made it work . Just added "other" functionality in edges and added both edges to edge list. Not sure if all other functions work properly again. 
          
        pq.add(e);
      }
    }
  }
  
ArrayList<Vertex> pathDijkstra;  
boolean x = true;
boolean showPath = false;
boolean drawing =true;
public void draw(){
  background(51);
  translate(50,50);
  int z=5;
   
   
 
 if(drawing){
   textSize(20);
   text("GENERATING MAZE", width/2-100, -30);
   while(z-->0){
  x = true;
  stroke(255,0,0);
  strokeWeight(2);
  if(!pq.isEmpty()){
      Edge e = pq.poll();
      Vertex v1 = e.v1; Vertex v2 = e.v2;
      if(v1.visited && v2.visited) x = false;
              
      if(x){
        mst.add(e);
        mstG.addEdge(e.v1.data,e.v2.data,1);
      if(!v1.visited)visit(v1);
      if(!v2.visited)visit(v2);}
    
    for(Vertex v : g.edgeL){
    v.show();
  }
  }
  else {
    drawing = false;
    timer = new Timer();
    timer.reset();
  }
  
 }
    for(Edge e1 : mst)
      e1.show(1);
    
    int x = mst.get(mst.size()-1).v1.x;
    int y = mst.get(mst.size()-1).v1.y;
    fill(0,255,255);
    noStroke();
    ellipse(x,y,15,15);
    
}

if(!drawing){
  timer.update();
  timer.show();
  
  
  
  textSize(20);
  fill(0,255,255);
  text(" 'S' for SOLUTION ( Penalty )", width/2-160, -30);
  textSize(30);
  fill(255,0,0);
  text("Points : " + points + " ( +" + (int)(timer.time +10) + " )", width/2+300, -27);
  
  
  
  for(Vertex v : mstG.edgeL){
    
    for(Edge e : v.edges){
      e.show(1);
    }
  }
  noFill();
  stroke(255);
  ellipse(end.x,end.y,10,10);
  b.show();
  
  if(b.v.data==end.data){
    fill(0,255,255,200);
    rect(width/2,height/2,displayWidth+100,displayHeight);
    textSize(34);
    fill(255,200);
    rect(width/2,height/2-110,400,50);
    fill(255,0,0);
    text("Click for new Target" ,width/2-170,height/2-100);
    if(mousePressed){
      end = mstG.getVertex((int)random(no-5)); 
      showPath = false;
      points+= (int)timer.time + 10;
      timer.reset();
      }
    else if(keyPressed && key==ENTER){
      end = mstG.getVertex((int)random(no-5)); 
      showPath = false;
      points+= (int)timer.time + 10;
      timer.reset();}
  }
  
  
    
  if(showPath){
    pathDijkstra = mstG.Dijkstra(b.v.data,end.data);
    for(int i = 0; i<pathDijkstra.size()-1; i++){
    PVector p1 = pathDijkstra.get(i).getCoord();
    PVector p2 = pathDijkstra.get(i+1).getCoord();
    strokeWeight(2);
    stroke(0,255,20);
    
    line(p1.x,p1.y,p2.x,p2.y);
    
  }
  
}
}
}


    
      
  
 public void keyPressed(){
   if(!drawing){ 
   if(keyCode==LEFT){
      //System.out.println("left");
      if(mstG.hasEdge(b.v.data,b.v.data - 25))
        b.update(mstG.getVertex(b.v.data - 25));
    }
    if(keyCode==RIGHT){
      //System.out.println("right");
      if(mstG.hasEdge(b.v.data,b.v.data + 25))
        b.update(mstG.getVertex(b.v.data + 25));
    }
    if(keyCode==DOWN){
      //System.out.println("down");
      if(mstG.hasEdge(b.v.data,b.v.data + 1))
        b.update(mstG.getVertex(b.v.data + 1));
    }
    if(keyCode==UP){
      //System.out.println("up");
      if(mstG.hasEdge(b.v.data,b.v.data - 1))
        b.update(mstG.getVertex(b.v.data - 1));
    }
    
    if(key=='s'){
    showPath=!showPath;
    timer.time-=10;}
   }
  }

  
class ball{
  Vertex v;
  PVector p;
  int x,y;
  ball(Vertex v){
    this.v = v;
    p = v.getCoord();
  }
  
  public void show(){
    noStroke();
    fill(0,255,0);
    x+= (p.x - x) * 0.7f;
    y+= (p.y -y) *0.7f;
    ellipse(x,y,20,20);
  }
  
  public void update(Vertex v){
    this.v=v;
    p = v.getCoord();
  }
}




class Vertex implements Comparable<Vertex>{
  int data;
  int cost = Integer.MAX_VALUE;
  int idcc = -1;
  HashSet<Edge> edges = new HashSet<Edge>();
  boolean visited=false;
  Vertex prev;
  int x;
  int y;
  Vertex(int data){
    this.data = data;
    this.x = (data/25)*52;
    this.y = (data%25)*33;
  }
  
  public int compareTo(Vertex v2){
    return +this.cost - v2.cost;
  }
  
  public String toString(){
    return "" + data;
  }
  public void show(){
    noFill();
    stroke(255,10);
    strokeWeight(1);
    ellipse(this.x,this.y,20,20);
    
    //fill(255);
    //textSize(15);
    //text("" + this.data,this.x-15,this.y+10);
  }
  public PVector getCoord(){
    return new PVector(this.x,this.y);
  }
}

class Edge implements Comparable<Edge>{
  Vertex v1,v2;
  int weight;
  
  Edge(Vertex v1, Vertex v2, int weight){
    this.v1 = v1;
    this.v2 = v2;
    this.weight = weight;
  }
  
  public String toString(){
    return v1 + "->" +v2 + " (" +weight+")";
  }
  
  public int compareTo(Edge e){
    return this.weight - e.weight;
  }
  public void show(int colo){
    PVector p1 = v1.getCoord();
    PVector p2 = v2.getCoord();
    strokeWeight(1);
    if(colo==0)stroke(255);
    else if(colo==1) {stroke(255,0,0); strokeWeight(15);}
    else if(colo==2) stroke(0,255,0);
    line(p1.x,p1.y,p2.x,p2.y);
  }
  
  public Vertex other(Vertex v) {
    if(v==v1) return v2;
    else if(v==v2) return v1;
    return null;
  }
}




class WeightedGraph{
  ArrayList<Vertex> edgeL = new ArrayList<Vertex>();
  boolean directed = false;
  public WeightedGraph(boolean dir) {
    // TODO Auto-generated constructor stub
    this.directed = dir;
  }
  
  public void addVertex(int data){
    if(getVertex(data)== null)
    edgeL.add(new Vertex(data));
  }
  public Edge getEdge(Vertex v1, Vertex v2){
  
    for(Edge edges : v1.edges)
      if(edges.v2 == v2)
        return edges;
    return null;
  }
  public void printList(){
    System.out.println("Graph Vertices and Neighbour list : ");
    for(Vertex v : edgeL){
      System.out.println(v + " : " + v.edges);
    }
  }
  public Vertex getVertex(int data){
    for(Vertex v : edgeL)
      if(v.data==data)
        return v;
    return null;
  }
  
  public boolean hasEdge(int d1, int d2){
    Vertex v1 = getVertex(d1);
    Vertex v2 = getVertex(d2);
    
    return hasEdge(v1,v2);
  }
    
  public boolean hasEdge(Vertex v1, Vertex v2){
    for(Edge edges : v1.edges)
      if(edges.v2 == v2)
        return true;
    return false;
  }
  public void addEdge(int data1, int data2,int weight){
    Vertex v1 = getVertex(data1);
    if(v1==null) throw new NullPointerException("Not found " + data1);
    Vertex v2 = getVertex(data2);
    if(v2==null) throw new NullPointerException("Not found " + data2);
    
    if(v1==v2) return;
    if(hasEdge(v1,v2)) return;
    Edge e = new Edge(v1,v2,weight);
    v1.edges.add(e);
    v2.edges.add(e);
    
    if(!this.directed){
      Edge e2 = new Edge(v2,v1,weight);
      v2.edges.add(e2);
      v1.edges.add(e2);
      
    }
  }
  public void unvisit(){
    for(Vertex v : edgeL){
      v.visited=false;
      v.cost=0;
      v.prev=null;
    }
  }
 
 
 public void constructPath(Vertex v2, ArrayList<Vertex> path){
    Stack<Vertex> s = new Stack<Vertex>();
    Vertex v = v2;
    while(v!=null){
      s.add(v);
      v=v.prev;
    }
    
    while(!s.isEmpty())
      path.add(s.pop());
  }
  
  
  public ArrayList<Vertex> Dijkstra(int data1, int data2){
    Vertex v1 = getVertex(data1);
    if(v1==null) throw new NullPointerException("Not found " + data1);
    Vertex v2 = getVertex(data2);
    if(v2==null) throw new NullPointerException("Not found " + data2);
    //System.out.println();
    unvisit();
    for(Vertex v : edgeL)
    {v.cost = Integer.MAX_VALUE;}
    ArrayList<Vertex> path = new ArrayList<Vertex>();
    if(Dijkstra(v1,v2,path));
      //System.out.println(" Dijkstra's (lowest cost) Path from " +  data1 + " to " + data2 +" : " + path);
    //else System.out.println("Path from " +  data1 + " to " + data2 + " Not found");
    
    //System.out.println("Cost : " + v2.cost);
    return path;
  }
  
  public boolean Dijkstra (Vertex v1, Vertex v2, ArrayList<Vertex> path){
    PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();
    v1.cost=0;
    v1.visited=true;
    pq.add(v1);
    while(!pq.isEmpty()){
      
      Vertex v = pq.poll();
      
      v.visited = true;
      
      if(v.data == v2.data){
        constructPath(v2, path);
        return true;
      }
      
      for(Edge edge : v.edges){
        Vertex n  = edge.v2;
        
        if(!n.visited){
          int tempCost = v.cost + edge.weight;
          
          if(tempCost < n.cost){
            
            if(pq.contains(n)) pq.remove(n);
            n.cost=tempCost;
            n.prev=v;
            
            pq.add(n);
          }
        }
      }
    }
    
    
    return false;
    
  }
}


class Timer{
  
  double time;
  int reward;
  public void reset(){
 
    pathDijkstra = mstG.Dijkstra(b.v.data,end.data);
    //reward = (int) (pathDijkstra.size() );
    time = pathDijkstra.size()*1.3f ;
  }
  
  public void update(){
    time-=0.04f;
    if(showPath)time-=0.18f;
  }
  
  public void show(){
    textSize(21);
    fill(255,200);
    rect(157,-40,350,40);
    fill(255,0,0);
    text("Time left : " +  time , -10,-27);
  }
}
  public void settings() {  size(1900,900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Prim_anim____maze_try" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
