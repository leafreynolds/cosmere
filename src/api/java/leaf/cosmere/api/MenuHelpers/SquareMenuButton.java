package leaf.cosmere.api.MenuHelpers;

public class SquareMenuButton extends MenuButton
{
	double width;
	double height;

	public SquareMenuButton(double posX, double posY)
	{
		this.width = 20;
		this.height = 20;

		this.x1 = posX - (width / 2);
		this.y1 = posY - (height / 2);

		//left side downer
		this.x2 = posX - (width / 2);
		this.y2 = posY + (height / 2);

		//right side downer
		this.x3 = posX + (width / 2);
		this.y3 = posY + (height / 2);

		//right side upper
		this.x4 = posX + (width / 2);
		this.y4 = posY - (height / 2);

		this.posX = posX;
		this.posY = posY;

		this.red = 235;
		this.blue = 20;
		this.green = 20;
		this.opacity = 200;
	}

}
