package C;

public class WBS {
	//Internal state
	private int WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE; 
	private int WBS_Node_WBS_BSCU_rlt_PRE1; 
	private int WBS_Node_WBS_rlt_PRE2;
	
	//Outputs
	private int Nor_Pressure;
	private int Alt_Pressure;
	private int Sys_Mode;
	
	public WBS() {
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;	
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
	}

	public double update(int PedalPos, boolean AutoBrake, boolean Skid) {

		double ret = 0;

		int WBS_Node_WBS_AS_MeterValve_Switch; 
		int WBS_Node_WBS_AccumulatorValve_Switch; 
		int WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch; 
		boolean WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator; 
		int WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1; 
		int WBS_Node_WBS_BSCU_Command_Switch; 
		boolean WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6; 
		int WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay; 
		int WBS_Node_WBS_BSCU_Switch2; 
		int WBS_Node_WBS_BSCU_Switch3; 
		int WBS_Node_WBS_BSCU_Unit_Delay1; 
		int WBS_Node_WBS_Green_Pump_IsolationValve_Switch; 
		int WBS_Node_WBS_SelectorValve_Switch; 
		int WBS_Node_WBS_SelectorValve_Switch1; 
		int WBS_Node_WBS_Unit_Delay2; 
		
	   WBS_Node_WBS_Unit_Delay2 = WBS_Node_WBS_rlt_PRE2; 
	   WBS_Node_WBS_BSCU_Unit_Delay1 = WBS_Node_WBS_BSCU_rlt_PRE1; 
	   WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay = WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE; 

	   WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator = (WBS_Node_WBS_BSCU_SystemModeSelCmd_Unit_Delay == 0); 

	   ret += Math.abs(PedalPos);
	   if ((PedalPos == 0)) {
		   	  Utils.path[0]++;		   	  
		      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
		   } else { 
			   ret += Math.abs(PedalPos - 1);
			   if ((PedalPos == 1)) {
				   Utils.path[1]++;
			      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 1;
			   }  else { 
				   ret += Math.abs(PedalPos - 2);
				   if ((PedalPos == 2)) {
					   Utils.path[2]++;   	  
				      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 2;
				   } else { 
					   ret += Math.abs(PedalPos - 3);
					   if ((PedalPos == 3)) { 
						   Utils.path[3]++;
					      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 3;
					   } else { 
						   ret += Math.abs(PedalPos - 4);
						   if ((PedalPos == 4)) {
							   Utils.path[4]++;
						      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 4;
						   }  else { 
							   Utils.path[5]++;
						      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
						   }
					   }
				   }
			   }
		   }
	   
	   if ((AutoBrake && 
		         WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)) {
		   Utils.path[6]++;
		      WBS_Node_WBS_BSCU_Command_Switch = 1; 
		   }  else {
			   Utils.path[7]++;
		      WBS_Node_WBS_BSCU_Command_Switch = 0;
		   }
	   
	   WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6 = ((((!(WBS_Node_WBS_BSCU_Unit_Delay1 == 0)) && 
	         (WBS_Node_WBS_Unit_Delay2 <= 0)) && 
	         WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator) || 
	         (!WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)); 

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      if (Skid) 
	      {
	    	  Utils.path[8]++;
	         WBS_Node_WBS_BSCU_Switch3 = 0;
	      }
	      else
	      {
	    	  Utils.path[9]++;
	         WBS_Node_WBS_BSCU_Switch3 = 4;
	      }
	   }
	   else {
		   Utils.path[10]++;
	      WBS_Node_WBS_BSCU_Switch3 = 4;
	    } 

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 0;
	      Utils.path[11]++;
	   }  else { 
	      WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 5;
	      Utils.path[12]++;
	    } 

	   if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) {
		   Utils.path[13]++;
	      WBS_Node_WBS_SelectorValve_Switch1 = 0; 
	   }
	   else { 
		   Utils.path[14]++;
	      WBS_Node_WBS_SelectorValve_Switch1 = 5; 
	   }

	   if ((!WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6)) {
		   Utils.path[15]++;
	      WBS_Node_WBS_AccumulatorValve_Switch = 0; 
	   }  else { 
		   if ((WBS_Node_WBS_SelectorValve_Switch1 >= 1)) {
			   Utils.path[16]++;
		      WBS_Node_WBS_AccumulatorValve_Switch = WBS_Node_WBS_SelectorValve_Switch1;
		   }
		   else { 
			   Utils.path[17]++;
		      WBS_Node_WBS_AccumulatorValve_Switch = 5;
		   }
	   }

	   if ((WBS_Node_WBS_BSCU_Switch3 == 0)) {
	      WBS_Node_WBS_AS_MeterValve_Switch = 0;
	      Utils.path[18]++;
	   }  else { 
		   if ((WBS_Node_WBS_BSCU_Switch3 == 1))  {
		      WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 4);
		      Utils.path[19]++;
		   }  else {  
			   if ((WBS_Node_WBS_BSCU_Switch3 == 2))  {
			      WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 2);
			      Utils.path[20]++;
			   }  else { 
				   if ((WBS_Node_WBS_BSCU_Switch3 == 3)) { 
				      WBS_Node_WBS_AS_MeterValve_Switch = ((WBS_Node_WBS_AccumulatorValve_Switch / 4) * 3);
				      Utils.path[21]++;
				   }  else { 
					   if ((WBS_Node_WBS_BSCU_Switch3 == 4)) { 
					      WBS_Node_WBS_AS_MeterValve_Switch = WBS_Node_WBS_AccumulatorValve_Switch;
					      Utils.path[22]++;
					   }  else { 
					      WBS_Node_WBS_AS_MeterValve_Switch = 0;
					      Utils.path[23]++;
					   }
				   }
			   }
		   }
	   }	   

	   if (Skid) {
	      WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = 0;
	      Utils.path[24]++;
	   }  else { 
	      WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = (WBS_Node_WBS_BSCU_Command_Switch+WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1);
	      Utils.path[25]++;
	   }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      Sys_Mode = 1;
	      Utils.path[26]++;
	   }  else { 
		   Utils.path[27]++;
	      Sys_Mode = 0;
	   }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      WBS_Node_WBS_BSCU_Switch2 = 0; 
	      Utils.path[28]++;
	   }  else { 
		   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 0) && 
		         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 1))) {					   
		      WBS_Node_WBS_BSCU_Switch2 = 0;
		      Utils.path[29]++;
		   } else { 
			   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 1) && 
			         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 2)))  {
				   Utils.path[30]++;
			      WBS_Node_WBS_BSCU_Switch2 = 1; 
			   }  else { 
				   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 2) && 
				         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 3))) {
					   Utils.path[31]++;
				      WBS_Node_WBS_BSCU_Switch2 = 2; 
				   } else { 
					   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 3) && 
					         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 4)))  {
						   Utils.path[32]++;
					      WBS_Node_WBS_BSCU_Switch2 = 3; 
					   } else { 
						   Utils.path[33]++;
					      WBS_Node_WBS_BSCU_Switch2 = 4;
					   }
				   }
			   }
		   }
	   }

	   if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1))  {
	      WBS_Node_WBS_SelectorValve_Switch = WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
	      Utils.path[34]++;
	   }  else { 
	      WBS_Node_WBS_SelectorValve_Switch = 0;
	      Utils.path[35]++;
	   }

	   if ((WBS_Node_WBS_BSCU_Switch2 == 0)) {
	      Nor_Pressure = 0; 
	      Utils.path[36]++;
	   }  else { 
		   if ((WBS_Node_WBS_BSCU_Switch2 == 1)) {
		      Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 4);
		      Utils.path[37]++;
		   }  else {
			   if ((WBS_Node_WBS_BSCU_Switch2 == 2)) {
			      Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 2);
			      Utils.path[38]++;
			   }  else { 
				   if ((WBS_Node_WBS_BSCU_Switch2 == 3)) {
				      Nor_Pressure = ((WBS_Node_WBS_SelectorValve_Switch / 4) * 3);
				      Utils.path[39]++;
				   } else { 
					   if ((WBS_Node_WBS_BSCU_Switch2 == 4)) { 
					      Nor_Pressure = WBS_Node_WBS_SelectorValve_Switch;
					      Utils.path[40]++;
					   } else { 
					      Nor_Pressure = 0;
					      Utils.path[41]++;
					   }
				   }
			   }
		   }
	   }

	   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 0)) {
	      Alt_Pressure = 0; 
	      Utils.path[42]++;
	   }  else {
		   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 1)) {
		      Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 4);
		      Utils.path[43]++;
		   }  else { 
			   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 2)) {
			      Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 2);
			      Utils.path[44]++;
			   } else { 
				   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 3)) {
				      Alt_Pressure = ((WBS_Node_WBS_AS_MeterValve_Switch / 4) * 3);
				      Utils.path[45]++;
				   } else { 
					   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 4)) {
					      Alt_Pressure = WBS_Node_WBS_AS_MeterValve_Switch;
					      Utils.path[46]++;
					   } else { 
					      Alt_Pressure = 0;
					      Utils.path[47]++;
					   }
				   }
			   }
		   }
	   }

	   WBS_Node_WBS_rlt_PRE2 = Nor_Pressure; 

	   WBS_Node_WBS_BSCU_rlt_PRE1 = WBS_Node_WBS_BSCU_Switch2;
	   if (WBS_Node_WBS_BSCU_rlt_PRE1 > 0)
		   System.out.println("WBS_Node_WBS_BSCU_rlt_PRE1 = " + WBS_Node_WBS_BSCU_rlt_PRE1);

	   WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = Sys_Mode; 

	   Utils.stopCriteria = true;
	   for (int i = 0; i < 48; i++)
		   if (Utils.path[i] == 0)
		   {
			   Utils.stopCriteria = false;
			   break;
		   }
	   return ret;
	}
	
	public static void launch(int pedal1, boolean auto1, boolean skid1, int pedal2, boolean auto2, boolean skid2, int pedal3, boolean auto3, boolean skid3) {
		WBS wbs = new WBS();
		wbs.update(pedal1, auto1, skid1);
		wbs.update(pedal2, auto2, skid2);
		wbs.update(pedal3, auto3, skid3);		
	}
	
	public static void main(String[] args) {		
		launch(0,false,false,0,false,false,0,false,false);
	}
}
