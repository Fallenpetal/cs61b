public class Planet{
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	private static final double G = 6.67e-11;
	public Planet(double xP,double yP,double xV,double yV,double m,String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img; 
	}
    public Planet(Planet p){
    	this(p.xxPos,p.yyPos,p.xxVel,p.yyVel,p.mass,p.imgFileName);
    }

	public double calcDistance(Planet p){
		return Math.pow((Math.pow((this.xxPos-p.xxPos),2)+Math.pow((this.yyPos-p.yyPos),2)),1.0/2);
	}

	public double calcForceExertedBy(Planet p){
		return (this.mass*p.mass*G)/Math.pow(this.calcDistance(p),2);

	}

	public double calcForceExertedByX(Planet p){
		return this.calcForceExertedBy(p)*(p.xxPos-this.xxPos)/this.calcDistance(p);
	}

	public double calcForceExertedByY(Planet p){
		return this.calcForceExertedBy(p)*(p.yyPos-this.yyPos)/this.calcDistance(p);
	}

	public double calcNetForceExertedByX(Planet[] p){
		double NetExertedByX=0;
		for(int i=0;i<p.length;i++){
			if(!this.equals(p[i])){
				NetExertedByX+=this.calcForceExertedByX(p[i]);
			}
		}
		return NetExertedByX;
	}

	public double calcNetForceExertedByY(Planet[] p){
		double NetExertedByY=0;
		for(int i=0;i<p.length;i++){
			if(!this.equals(p[i])){
				NetExertedByY+=this.calcForceExertedByY(p[i]);
			}
		}
		return NetExertedByY;
	}

	public void update(double dt,double fX,double fY){
		double aX=0,aY=0;
		aX=fX/this.mass;
		aY=fY/this.mass;
		this.xxVel=this.xxVel+aX*dt;
		this.yyVel=this.yyVel+aY*dt;
		this.xxPos=this.xxPos+this.xxVel*dt;
		this.yyPos=this.yyPos+this.yyVel*dt;
	}

}