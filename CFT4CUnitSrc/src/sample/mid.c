int middle(int X, int Y, int Z)
{
	int mid = Z;
	
	if (Y < Z) {
		if (X < Y)
			mid = Y;
		else
			if (X < Z)
				mid = X;
	}
	else {
		if (X >= Y)
			mid = Y;
		else
			if (X > Z)
				mid = X;
	}

	return mid;
}