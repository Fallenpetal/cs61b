public class NBody{

	public static double readRadius(String path){
		In in =new In(path);
		int firstinteger=in.readInt();
		double ReadRadius=in.readDouble();
		return ReadRadius;
	}

	public static Planet[] readPlanets(String path){
		In in=new In(path);
		int numbersum=in.readInt();		
		Planet[] p =new Planet[numbersum];
		double ReadRadius=in.readDouble();
		
		for(int i=0;i<numbersum;i++){
			double xxPos=in.readDouble();
			double yyPos=in.readDouble();
			double xxVel=in.readDouble();
			double yyVel=in.readDouble();
			double mass=in.readDouble();
			String img=in.readString();
			p[i]= new Planet(xxPos,yyPos,xxVel,yyVel,mass,img);
		}
		return p;
	}

	public static void main(String[] args){
		double T=Double.parseDouble(args[0]);
		double dt=Double.parseDouble(args[1]);
		String filename=args[2];
		double R=readRadius(filename);
		Planet[] p=readPlanets(filename);
		StdDraw.enableDoubleBuffering();
		for(int t=0;t<=T;t+=dt){
			double xForce[]=new double[5];
			double yForce[]=new double[5];
			for(int i=0;i<p.length;i++){
				xForce[i]=p[i].calcNetForceExertedByX(p);
				yForce[i]=p[i].calcNetForceExertedByY(p);
			}
			for(int j=0;j<p.length;j++){
				p[j].update(dt,xForce[j],yForce[j]);
			}
			String img="images/starfield.jpg";
			StdDraw.setScale(-R,R);
			StdDraw.clear();
			StdDraw.picture(0,0,img);
			for(int k=0;k<p.length;k++){
				p[k].Draw();
			}
				StdDraw.show();
				StdDraw.pause(10);
		}
		StdOut.printf("%d\n", p.length);
		StdOut.printf("%.2e\n", R);
		for (int i = 0; i < p.length; i++) {
    	StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  p[i].xxPos, p[i].yyPos, p[i].xxVel,
                  p[i].yyVel, p[i].mass, p[i].imgFileName);   
}
	}
}