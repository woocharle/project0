package com.ict.model;

import java.util.Map;

public class Tank {
	private Map<String, String> em_map;
	private double pi = Math.PI;
	
	// cal Area1는 Drywall의 면적
	public double calArea1(String body, String head, String part, double tdia, double tlen, double wtlen) {
		double res = 0;
		double dlen = 0;
		double el = 0;   //ellips의 비율  
		double elarea = 0;// ellpis의 넓이

		if(head.equals("ellips1")) {
			el = 2;
		} else if(head.equals("ellips2")) {
			el = 3;
		} 
		
		elarea = (el == 2 || el == 3) ? 2 * pi * (tdia * (el + 1) / el / 4) * (tdia / 2 / el) : 0;
		
		if (body.equals("vertical1") || body.equals("vertical2")) {
			res = part.equals("dry") ? pi * tdia * (tlen - wtlen) : pi * tdia * (wtlen);
					
			if(head.equals("plate")) {
				res = res + Math.pow(tdia, 2) / 4; 
			} else if (head.equals("ellips1") || head.equals("ellips2")) {
				res = res + elarea;
			}
		
		} else if(body.equals("horizontal") ) {
			double s_total = pi * Math.pow(tdia, 2) / 4;
			double theta = 0;
			double s_dry = 0;
			
			if(wtlen == tdia) {
				s_dry = 0;
				
			} else if(wtlen > tdia / 2){
				theta = 2 * Math.acos((tdia/2 - (wtlen - tdia/2))/ (tdia/2)) * 180 / pi;
				s_dry = s_total * (360 - theta) / 360 + Math.pow(tdia / 2, 2) * 0.5 * Math.sin(pi * theta / 180);
			
			} else if(wtlen == tdia/2) {
				s_dry = s_total / 2;
			}
			else {
				theta = 2 * Math.acos((tdia/2 - (tdia/2 - wtlen))/ (tdia/2)) * 180 / pi;
				s_dry = s_total - (s_total * theta / 360 - Math.pow(tdia / 2, 2) * 0.5 * Math.sin(pi * theta / 180));
			}
			
			res = part.equals("dry") ? (pi * tdia * tlen + 2 * elarea) * s_dry / s_total :
						   (pi * tdia * tlen + 2 * elarea) * (s_total - s_dry) / s_total ;	
			
		} else if(body.equals("sphere") ) {
			if(wtlen == tdia) {
				res = part.equals("dry") ? 0 : 4 * pi * Math.pow(wtlen/2, 2);
				
			}else if(wtlen > tdia / 2){
				dlen = tlen - tdia / 2;
				res = part.equals("dry") ? 2 * pi * Math.pow(tdia/2, 2) - 2 * pi * tdia / 2 * dlen :
										   2 * pi * Math.pow(tdia/2, 2) + 2 * pi * tdia / 2 * dlen ;
				
			}else if(wtlen == tdia / 2) {
				res = 2 * pi * Math.pow(tdia/2, 2);
						
			}else if(wtlen < tdia / 2 && wtlen > 0) {
				dlen = tdia / 2 - tlen;
				res = part.equals("dry") ? 2 * pi * Math.pow(tdia/2, 2) + 2 * pi * tdia / 2 * dlen : 
					 					   2 * pi * Math.pow(tdia/2, 2) - 2 * pi * tdia / 2 * dlen ;
				
				
			}else {
				res = part.equals("dry") ? 4 * pi * Math.pow(wtlen/2, 2) : 0;
			}
		}
		
		res =  ((int)(res * 100)) / 100.0;
		
		return res;
	}	
	
	public double calArea2 (double tdia ,double troof) {
		return ((int)100 * pi * tdia / 2 * Math.pow((Math.pow(tdia, 2) / 4 + Math.pow(troof, 2)), 0.5)) / 100.0;  
	}
	
	public double calArea3 (double tdia) {
		return ((int)pi * Math.pow(tdia, 2) / 4) / 100.0;
	}
	
	
	
	// Temp와 U값은 고정점 반복법 사용 [ abs(이전값 - 현재값) / 현재값 구하고 싶은 값을 변수로 넣어서 result 변화.]
	
	public double calResult(String target, String part, double temp, double temp_sur, double vis, double den, double exp, 
							double tw, double hi, double cp, double th, double em, double vel_wind,
							double thick_body, double thcon_body, double thick_insul, double thcon_insul, double coeff_f) {
		double res = 0;
		double temp_res = 0;
		double u0 = 0;
		double u1 = 0;
		double u2 = 0;
 		double t1 = 0;
 		double t2 = 0;
		
		temp_res = (temp / temp_sur) / 2;
		
		double gr = calGr(temp_res, vis, den, exp, tw, hi);
		double pr = calPr(cp, vis, th);
		
		double coeff = 0;
		double coeff1 = calCoeff1(gr, pr, hi, th);
		double coeff2 = calCoeff2(gr, pr, hi, th);
		double coeff3 = calCoeff3(gr, pr, hi, th);
		double coeff4 = calCoeff4(gr, pr, hi, th);
		double coeff5 = calCoeff5(gr, pr, hi, th);
		double coeff6 = calCoeff6(gr, pr, hi, th);
		double coeff_rad = calCoeff7(temp_res, temp_sur, em);
		
		
		switch (part) {
			case "dry": coeff = Math.max(Math.max(coeff1, coeff2), coeff4);  break;
			case "wet": coeff = Math.max(Math.max(coeff1, coeff3), coeff4);  break;
			case "roof": coeff = coeff5;  break;
			case "btn": coeff = coeff6;  break;
		}
		
		double wind_f = 0.868*Math.pow(Math.abs(temp - temp_sur),-0.246) * vel_wind * 0.44704;
		double coeff_w = Math.max(Math.max(coeff1, coeff2), coeff4) * wind_f;
		
		
		double error = 0;
		
		t1 = temp_res;
		
		do {
			
		
			error = (u1- u2)/u2; 
			t1 = t2;
			u1 = u2;
			
		}while(error > 0.01);
		
		
		res = (target.equals("temp")) ? temp_res : u0;
		
		res =  ((int)(res * 100)) / 100.0;
		
		return res;
	}
	
	public double calResult2(String part, double temp, double temp_sur, double vis, double den, double exp, 
			double tw, double hi, double cp, double th, double em, double vel_wind,
			double thick_body, double thcon_body, double thick_insul, double thcon_insul, double coeff_f) {
		double res = 0;
		double temp_res = 0;
		double u = 0;

		
		temp_res = (temp / temp_sur) / 2;
		
		double gr = calGr(temp_res, vis, den, exp, tw, hi);
		double pr = calPr(cp, vis, th);
		
		double coeff = 0;
		double coeff1 = calCoeff1(gr, pr, hi, th);
		double coeff2 = calCoeff2(gr, pr, hi, th);
		double coeff3 = calCoeff3(gr, pr, hi, th);
		double coeff4 = calCoeff4(gr, pr, hi, th);
		double coeff5 = calCoeff5(gr, pr, hi, th);
		double coeff6 = calCoeff6(gr, pr, hi, th);
		double coeff_rad = calCoeff7(temp_res, temp_sur, em);
		
		
		switch (part) {
			case "dry": coeff = Math.max(Math.max(coeff1, coeff2), coeff4);  break;
			case "wet": coeff = Math.max(Math.max(coeff1, coeff3), coeff4);  break;
			case "roof": coeff = coeff5;  break;
			case "btn": coeff = coeff6;  break;
		}
		
		double wind_f = 0.868*Math.pow(Math.abs(temp - temp_sur),-0.246) * vel_wind * 0.44704;
		double coeff_w = Math.max(Math.max(coeff1, coeff2), coeff4) * wind_f;

	
		return u;
	}
	
	//
	
	public double calGr(double t, double vis, double den, double exp, double tw, double hi) {
		return Math.abs(t - tw) * Math.pow(hi, 3) * Math.pow(den, 2) * 4.17 * 100000000 * exp 
				/ Math.pow(2.42 * vis, 2) ; 
	}
	
	public double calPr(double cp, double vis, double th) {
		return 2.42 * vis * cp / th;
	}

	
	// k는 thermal conductivity를 의미한다. 
	
	public double calCoeff1(double gr, double pr, double len, double k) {
		double res = 0;
		
		if (pr > 0.1 && pr < 40 && gr > Math.pow(10,9)) {
			res = 0.138 * Math.pow(gr, 0.36) * (Math.pow(pr, 0.175) - 0.55) * k / len;
		}else {
			res = 0;
		}
		
		return res;
	}
	
	public double calCoeff2(double gr, double pr, double len, double k) {
		double res = 0;
		
		if (pr > 4.5 && pr < 5.5 && pr * gr > Math.pow(10,9)) {
			res = 0.0295 * Math.pow(gr, 0.4) * Math.pow(pr, 0.47) * Math.pow((1 + 0.5 * Math.pow(pr, 0.67)), -0.4) * k / len;
		
		}else {
			res = 0;
		}
		
		return res;
	}
	
	public double calCoeff3(double gr, double pr, double len, double k) {
		return 0.495 * Math.pow((gr * pr), 0.25) * k / len;
	}
	
	public double calCoeff4(double gr, double pr, double len, double k) {
		return 0.45 * k * Math.pow(len, -0.75) * Math.pow((gr * pr), 0.25);
	}
	
	public double calCoeff5(double gr, double pr, double len, double k) {
		return 0.27 * k * Math.pow((gr * pr), 0.25) / len;
	}
	
	public double calCoeff6(double gr, double pr, double len, double k) {
		return 0.14 * k * Math.pow((gr * pr), 0.33) / len;
	}
	
	public double calCoeff7(double tw, double ta, double e) {
		return 0.1713 * e * Math.pow(10, -8) * (Math.pow((tw + 460), 4) - Math.pow((ta + 460), 4))/ (tw - ta); 
	}
	
	public double calCoeff_over(double coeff, double coeff_rad, double coeff_w, 
							double coeff_b, double coeff_i, double coeff_f) {
		double res = 0;
		
		return res; 
	}
	
	public double caltank(double thcon_tank, double thick_tank) {
		double res = 0;
		
		if(thick_tank == 0) {
			res = 0;
		}else {
			res = thcon_tank / thick_tank * 12;
		}
		
		return res;
	}
	
	
	
	public Map<String, String> getEm_map() {
		return em_map;
	}

	public void setEm_map(Map<String, String> em_map) {
		this.em_map = em_map;
	}
	
}