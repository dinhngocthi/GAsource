void computeTax (int status, double income) 
{
	// Compute tax
	double tax1 = income * 0.10;
	double tax2 = 8350 * 0.10 + (income - 8350) * 0.15;
	double tax3 = 8350 * 0.10 + (33950 - 8350) * 0.15 + (income - 33950) * 0.25;
	double tax4 = 8350 * 0.10 + (33950 - 8350) * 0.15 + (82250 - 33950) * 0.25 + (income - 82250) * 0.28;
	double tax5 = 8350 * 0.10 + (33950 - 8350) * 0.15 + (82250 - 33950) * 0.25 + (171550 - 82250) * 0.28 + (income - 171550) * 0.33;
	double tax6 = 8350 * 0.10 + (33950 - 8350) * 0.15 + (82250 - 33950) * 0.25 + (171550 - 82250) * 0.28 + (372950 - 171550) * 0.33 + (income - 372950) * 0.35;
	if (status == 0) 
	{ 
		// Compute tax for single filers
		if (income <= 8350)
			tax = tax1;
		else if (income <= 33950)
				tax = tax1;
			else if (income <= 82250)
					tax = tax3;
				else if (income <= 171550)
						tax = tax4;
					else if (income <= 372950)
							tax = tax5;
						else
							tax = tax6;
	}
	else if (status == 1) 
	{
		printf("Compute tax for married file jointly or qualifying widow(er)\n");
	}
	else if (status == 2) 
	{ 
		printf("Compute tax for married separately\n");
	}
	else if (status == 3) 
	{
		printf("Compute tax for head of household\n");
	}
	else 
	{
		printf("Error: invalid status\n");
	}

	// Display the result
	printf("Tax is %d\n", (int)(tax * 100) / 100.0);
}