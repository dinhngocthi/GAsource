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
	public int[] path; 
	
	public WBS() {
		WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = 0;	
		WBS_Node_WBS_BSCU_rlt_PRE1 = 0;
		WBS_Node_WBS_rlt_PRE2 = 100;
		Nor_Pressure = 0;
		Alt_Pressure = 0;
		Sys_Mode = 0;
		
		path = new int[100];
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
		   	  path[0]++;
		      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
		   } else { 
			   ret += Math.abs(PedalPos - 1);
			   if ((PedalPos == 1)) {
				  path[1]++;
			      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 1;
			   }  else { 
				   ret += Math.abs(PedalPos - 2);
				   if ((PedalPos == 2)) {
					   path[2]++;
				      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 2;
				   } else { 
					   ret += Math.abs(PedalPos - 3);
					   if ((PedalPos == 3)) { 
						   path[3]++;
					      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 3;
					   } else { 
						   ret += Math.abs(PedalPos - 4);
						   if ((PedalPos == 4)) {
							   path[4]++;
						      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 4;
						   }  else { 
							   path[5]++;
						      WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 = 0;
						   }
					   }
				   }
			   }
		   }
	   
	   if ((AutoBrake && 
		         WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)) {
		      WBS_Node_WBS_BSCU_Command_Switch = 1; 
		   }  else { 
		      WBS_Node_WBS_BSCU_Command_Switch = 0;
		   }
	   
	   WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6 = ((((!(WBS_Node_WBS_BSCU_Unit_Delay1 == 0)) && 
	         (WBS_Node_WBS_Unit_Delay2 <= 0)) && 
	         WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator) || 
	         (!WBS_Node_WBS_BSCU_Command_Is_Normal_Relational_Operator)); 

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      if (Skid) 
	         WBS_Node_WBS_BSCU_Switch3 = 0; 
	      else 
	         WBS_Node_WBS_BSCU_Switch3 = 4; 
	   }
	   else { 
	      WBS_Node_WBS_BSCU_Switch3 = 4;
	    } 

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 0; 
	   }  else { 
	      WBS_Node_WBS_Green_Pump_IsolationValve_Switch = 5; 
	    } 

	   if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1)) { 
	      WBS_Node_WBS_SelectorValve_Switch1 = 0; 
	   }
	   else { 
	      WBS_Node_WBS_SelectorValve_Switch1 = 5; 
	   }

	   if ((!WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6)) {
	      WBS_Node_WBS_AccumulatorValve_Switch = 0; 
	   }  else { 
		   if ((WBS_Node_WBS_SelectorValve_Switch1 >= 1)) { 
		      WBS_Node_WBS_AccumulatorValve_Switch = WBS_Node_WBS_SelectorValve_Switch1;
		   }
		   else { 
		      WBS_Node_WBS_AccumulatorValve_Switch = 5;
		   }
	   }

	   if ((WBS_Node_WBS_BSCU_Switch3 == 0)) {
	      WBS_Node_WBS_AS_MeterValve_Switch = 0;
	   }  else { 
		   if ((WBS_Node_WBS_BSCU_Switch3 == 1))  {
		      WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 4);
		   }  else {  
			   if ((WBS_Node_WBS_BSCU_Switch3 == 2))  {
			      WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 2);
			   }  else { 
				   if ((WBS_Node_WBS_BSCU_Switch3 == 3)) { 
				      WBS_Node_WBS_AS_MeterValve_Switch = ((WBS_Node_WBS_AccumulatorValve_Switch / 4) * 3);
				   }  else { 
					   if ((WBS_Node_WBS_BSCU_Switch3 == 4)) { 
					      WBS_Node_WBS_AS_MeterValve_Switch = WBS_Node_WBS_AccumulatorValve_Switch;
					   }  else { 
					      WBS_Node_WBS_AS_MeterValve_Switch = 0;
					   }
				   }
			   }
		   }
	   }	   

	   if (Skid) {
	      WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = 0;
	   }  else { 
	      WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch = (WBS_Node_WBS_BSCU_Command_Switch+WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1);
	   }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      Sys_Mode = 1; 
	   }  else { 
	      Sys_Mode = 0;
	   }

	   if (WBS_Node_WBS_BSCU_SystemModeSelCmd_Logical_Operator6) {
	      WBS_Node_WBS_BSCU_Switch2 = 0; 
	   }  else { 
		   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 0) && 
		         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 1))) {					   
		      WBS_Node_WBS_BSCU_Switch2 = 0; 
		   } else { 
			   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 1) && 
			         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 2)))  {
			      WBS_Node_WBS_BSCU_Switch2 = 1; 
			   }  else { 
				   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 2) && 
				         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 3))) {
				      WBS_Node_WBS_BSCU_Switch2 = 2; 
				   } else { 
					   if (((WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch >= 3) && 
					         (WBS_Node_WBS_BSCU_Command_AntiSkidCommand_Normal_Switch < 4)))  {
					      WBS_Node_WBS_BSCU_Switch2 = 3; 
					   } else { 
					      WBS_Node_WBS_BSCU_Switch2 = 4;
					   }
				   }
			   }
		   }
	   }

	   if ((WBS_Node_WBS_Green_Pump_IsolationValve_Switch >= 1))  {
	      WBS_Node_WBS_SelectorValve_Switch = WBS_Node_WBS_Green_Pump_IsolationValve_Switch;
	   }  else { 
	      WBS_Node_WBS_SelectorValve_Switch = 0;
	   }

	   if ((WBS_Node_WBS_BSCU_Switch2 == 0)) {
	      Nor_Pressure = 0; 
	   }  else { 
		   if ((WBS_Node_WBS_BSCU_Switch2 == 1)) {
		      Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 4);
		   }  else {
			   if ((WBS_Node_WBS_BSCU_Switch2 == 2)) {
			      Nor_Pressure = (WBS_Node_WBS_SelectorValve_Switch / 2);
			   }  else { 
				   if ((WBS_Node_WBS_BSCU_Switch2 == 3)) {
				      Nor_Pressure = ((WBS_Node_WBS_SelectorValve_Switch / 4) * 3);
				   } else { 
					   if ((WBS_Node_WBS_BSCU_Switch2 == 4)) { 
					      Nor_Pressure = WBS_Node_WBS_SelectorValve_Switch;
					   } else { 
					      Nor_Pressure = 0;
					   }
				   }
			   }
		   }
	   }

	   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 0)) {
	      Alt_Pressure = 0; 
	   }  else {
		   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 1)) {
		      Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 4); 
		   }  else { 
			   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 2)) {
			      Alt_Pressure = (WBS_Node_WBS_AS_MeterValve_Switch / 2);
			   } else { 
				   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 3)) {
				      Alt_Pressure = ((WBS_Node_WBS_AS_MeterValve_Switch / 4) * 3);
				   } else { 
					   if ((WBS_Node_WBS_BSCU_Command_PedalCommand_Switch1 == 4)) {
					      Alt_Pressure = WBS_Node_WBS_AS_MeterValve_Switch; 
					   } else { 
					      Alt_Pressure = 0;
					   }
				   }
			   }
		   }
	   }

	   WBS_Node_WBS_rlt_PRE2 = Nor_Pressure; 

	   WBS_Node_WBS_BSCU_rlt_PRE1 = WBS_Node_WBS_BSCU_Switch2; 

	   WBS_Node_WBS_BSCU_SystemModeSelCmd_rlt_PRE = Sys_Mode; 

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
