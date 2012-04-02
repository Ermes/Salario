package com.ermes.salario;

public class pagas {
	private  int min25,mas25,min3,cat,lab;
	private static int PENSIONISTA = 2;
	private static int DESEMPLEADO = 3;
	public static int SITUACION = 3;
	public  int N_PAGAS;
	public  int BRUTO;
	public double BRUTO_MENSUAL;
	public static final double SS = 6.35;
	public final double RED20;
	public final double RNT;
	public final double RNTREDU;
	public final double BASE;
	public final double REDU;
	public static final int MIN_PERSONAL = 5151;
	public static final int [] DESCENDIENTES = {0,1836,3876,7548,11730,15912};
	public static final double [][] SITUACION_FAMILIAR =
	{
		{0,0,0},
		{0,13662.00,15617.00},
		{13335.00,14774.00,16952.00},
		{11162.00,11888.00,12519.00}		
	};
	public static final double[][] T_IRPF = 
		{
			{0,0},
			{17707.20,24.75},
			{33007.20,30},
			{53407.2,40},
			{120000.20,47},
			{175000.20,49}
		};
		
	public static final double [][] BASE_SS = 
		{
		{0,0},
		{1045.20,3230.10},
		{867.00,3230.10},
		{754.20,3230.10},
		{748.20,3230.10},
		{748.20,3230.10},
		{748.20,3230.10},
		{748.20,3230.10},
		};
	
	///////// ---------- CONSTRUCTOR ------------ ////////////
	public pagas (int b,int nP,int m25,int x25,int m3, int c, int l) {
		N_PAGAS = nP;
		min25 = m25;
		mas25 = x25;
		min3 = m3;
		cat = c;
		lab = l;
		BRUTO = b;
		BRUTO_MENSUAL = b / nP;
		RNT = trunca(BRUTO - deduccionSS());
		RED20 = rtosTrabajo();
		RNTREDU = trunca((RNT - RED20) > 0 ? RNT - RED20 : 0);
		REDU = reduccionLaboral();
		BASE = RNTREDU - REDU;
	}
	
	///////// ---------- METODOS ------------ ////////////
	public double rtosTrabajo() {
		double rtos;
		if (RNT <= 9180.00) {
			rtos = 4080.00;
		} else if (RNT <= 13260.00) {
			rtos = 4080.00 - (0.35 * ( RNT - 9180.00));
		} else {
			rtos = 2652;
		}
		return trunca(rtos);
	}
	public double reduccionLaboral() {
		if ( lab == PENSIONISTA) {
			return 600;
		} else if ( lab == DESEMPLEADO ) {
			return 1200;
		} else {
			return 0;
		}
	}
	
	public  int getMIN_PERSONAL() {
		int m = 0;
		m= MIN_PERSONAL + (int)(DESCENDIENTES[min25+mas25] + min3*2244)/2;
		return m;
	};
	public  double deduccionSS(){
		double d;
		if ( BRUTO > BASE_SS[cat][0] ) {
			d = ( BRUTO < (BASE_SS[cat][1] * 12) ) ? (BRUTO * SS / 100) : (BASE_SS[cat][1] * 12 * SS / 100);
		} else {
			d = BASE_SS[cat][0];
		}
		return trunca(d);
	}

	public double retencionIRPF() {
		double baseIRPF = BASE;
		double irpf=0;

		for (int i = 1;i<6;i++){
			if (baseIRPF>T_IRPF[i][0]) {
				irpf +=(T_IRPF[i][0]-T_IRPF[i-1][0])*T_IRPF[i][1]/100; 
			} else {
				irpf +=(baseIRPF-T_IRPF[i-1][0])*T_IRPF[i][1]/100; 
				break;
			}
		}
		return trunca(irpf);
	}
	public double deduccionFamiliar() {
		double fam=0;		
		int basePersonal = getMIN_PERSONAL();

		for (int i = 1;i<6;i++){
			if (basePersonal>T_IRPF[i][0]) {
				fam +=(T_IRPF[i][0]-T_IRPF[i-1][0])*T_IRPF[i][1]/100; 
			} else {
				fam +=(basePersonal-T_IRPF[i-1][0])*T_IRPF[i][1]/100; 
				break;
			}
		}
		return trunca(fam);
	}
	
	public double aPagarIRPF() {
		return BRUTO * getIRPF() / 100;
	}
	public double rendimientoNetoReducido() {
		
		return 0;
	}
	public  double getIRPF() {
		double irpfAnual = trunca(retencionIRPF() - deduccionFamiliar());
		double limite = min43();
		
		double cuota = (irpfAnual > limite) ? limite : irpfAnual;
		
		if ( art80() > 0 ) {
			return trunca( (cuota)>0 ? trunca((cuota)*100/BRUTO) : 0 );
		} else {
			return Math.round( (cuota)>0 ? trunca((cuota)*100/BRUTO) : 0 );
		}
		
	}
	
	public double art80() {
		if ( BASE <= 8000) {
			return 400;
		} else if (BASE <= 12000) {
			return trunca( 400 - 0.1 * ( BASE - 8000 ) );
		} else {
			return 0;
		}
	}
	public double min43() {
		int hijos = (mas25+min25 >=2) ? 2 : mas25+min25 ;
		double limite = art80() + ( BRUTO - (SITUACION_FAMILIAR[SITUACION][hijos] + reduccionLaboral()) ) * 0.43;
		return limite;
	}
	public  double netoMensual() {
		double anualIRPF = aPagarIRPF();	
		// return trunca( bIRPF() - ( bIRPF()*getIRPF()/100 ) - (bSS()*SS/100) );
		return trunca( BRUTO_MENSUAL - ( anualIRPF/N_PAGAS ) - (deduccionSS()/12) );
	}
	
	public  double pagaExtra() {
		double anualIRPF = aPagarIRPF();
		return trunca( BRUTO_MENSUAL - ( anualIRPF/N_PAGAS ) );
		// return trunca(bIRPF() - (bIRPF()*getIRPF()/100) - (BRUTO/N_PAGAS)*SS/100 + (BRUTO/12)*SS/100);
	}
	
	private  double trunca(double d){
		
			return (Math.ceil(d*100))/100;
	}
}
