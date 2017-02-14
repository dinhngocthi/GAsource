void computeTax (int status, double income) 
{
	// Compute tax
	double tax = 0;
	if (status == 0) 
	{ 
		// Compute tax for single filers
		if (income <= 8350)
			tax = income * 0.10;
		else if (income <= 33950)
				tax = 8350 * 0.10 + (income - 8350) * 0.15;
			else if (income <= 82250)
					tax = 8350 * 0.10 + (33950 - 8350) * 0.15 + (income - 33950) * 0.25;
				else if (income <= 171550)
						tax = 8350 * 0.10 + (33950 - 8350) * 0.15 + (82250 - 33950) * 0.25 + (income - 82250) * 0.28;
					else if (income <= 372950)
							tax = 8350 * 0.10 + (33950 - 8350) * 0.15 + (82250 - 33950) * 0.25 + (171550 - 82250) * 0.28 + (income - 171550) * 0.33;
						else
							tax = 8350 * 0.10 + (33950 - 8350) * 0.15 + (82250 - 33950) * 0.25 + (171550 - 82250) * 0.28 + (372950 - 171550) * 0.33 + (income - 372950) * 0.35;
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