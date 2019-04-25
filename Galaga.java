import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.ArrayList;

public class Galaga extends Application implements EventHandler<InputEvent>
{

	GraphicsContext gc;
	Image spaceship;
	AnimateObjects animate;
	int shipX = 0;
	Canvas canvas;

	public static void main(String[] args)
	{
		launch();
	}

	public void start(Stage stage)
	{
		stage.setTitle("Final Project Title");
		Group root = new Group();
		canvas = new Canvas(925/*was850*/, 920);
		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		spaceship = new Image("spaceship.png");

		Ship control = new Ship(425, 795);

		gc.drawImage( control.getImage(), 425, 795 ); //runs

		animate = new AnimateObjects();
		animate.start();
		stage.show();
		scene.addEventHandler(KeyEvent.KEY_PRESSED,this);
		scene.addEventHandler(KeyEvent.KEY_RELEASED,this);

		URL resource = getClass().getResource("blop.wav");
		AudioClip clip = new AudioClip(resource.toString());
	}
	URL resource = getClass().getResource("blop.wav");
	AudioClip clip = new AudioClip(resource.toString());

	URL resource2 = getClass().getResource("metal.wav");
	AudioClip clip2 = new AudioClip(resource2.toString());

	int score = 0;
	boolean end = false;

	ArrayList<Integer> limitTL = new ArrayList<Integer>();
	ArrayList<Integer> limitTR = new ArrayList<Integer>();

	Ship control = new Ship(425, 795);
	boolean startGame = false;
	boolean checkHit = false;

	boolean createShot = false;
	int shotStart;
	ArrayList<ShipShot> shipShot = new ArrayList<ShipShot>();

	ArrayList<SpriteShot> spriteShot = new ArrayList<SpriteShot>();

	boolean startBflyL = false;
	boolean startBflyR = false;
	boolean startBeeT = false;

	ArrayList<Bee> bees = new ArrayList<Bee>();
	ArrayList<Integer> oddEven = new ArrayList<Integer>();
	ArrayList<Boolean> beeBoolean = new ArrayList<Boolean>();
	int countBees = 0;

	int countBflyL = 0;
	ArrayList<Butterfly> bfliesL = new ArrayList<Butterfly>();
	ArrayList<Integer> bflyOddEvenL = new ArrayList<Integer>();
	int countBflyR = 0;
	ArrayList<Butterfly> bfliesR = new ArrayList<Butterfly>();
	ArrayList<Integer> bflyOddEvenR = new ArrayList<Integer>();

	int countBeeT = 0;
	int rotateBeeT = 0;
	ArrayList<Bee> beeT = new ArrayList<Bee>();
	ArrayList<Integer> beeTNum = new ArrayList<Integer>();

	ArrayList<Integer> countStrafeBee = new ArrayList<Integer>();
	ArrayList<Integer> countStrafeBflyL = new ArrayList<Integer>();
	ArrayList<Integer> countStrafeBflyR = new ArrayList<Integer>();
	ArrayList<Integer> countStrafeBeeT = new ArrayList<Integer>();
	boolean strafeBee = false;
	boolean strafeBflyL = false;
	boolean strafeBflyR = false;
	boolean strafeBeeT= false;
	boolean strafeL = false;
	boolean strafeR = true;

	boolean enemyShooter = false;
	boolean enemyBL = false;
	boolean enemyBR = false;
	boolean enemyBT = false;

	int xOrig;
	int yOrig;
	int enemyIndex;
	int turns = 0;
	boolean offScreen = false;
	boolean comeBack = false;

	ImageView iv;
	SnapshotParameters params = new SnapshotParameters();
	Image rotatedImage;

	ArrayList<BeeShot> beeShot = new ArrayList<BeeShot>();
	boolean moveLeft = false;
	boolean moveRight = false;

	double timer = 0;
	double shotTimer = 0;

	public class AnimateObjects extends AnimationTimer
	{
		public void handle(long now)
		{
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

			if(!startGame)
			{
				gc.setFill(Color.WHITE);
				gc.setStroke(Color.WHITE );
				gc.setLineWidth(1);
				Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
				gc.setFont( font );
				gc.fillText("PRESS SPACE TO START", 165, 475 );
				gc.strokeText("PRESS SPACE TO START", 165, 475 );
				gc.setFill(Color.BLACK);
			}

			if(startGame)
			{
				timer+=0.0166666666;
				gc.setFill(Color.WHITE);
				gc.setStroke(Color.WHITE ); //Changes the outline the black
				gc.setLineWidth(1); //How big the black lines will be
				Font font = Font.font( "Arial", FontWeight.NORMAL, 48 );
				gc.setFont( font );
				gc.fillText("Score: " + score, 50, 75 ); //draws the yellow part of the text
				gc.strokeText("Score: " + score, 50, 75 ); //draws the outline part of the text
				gc.setFill(Color.BLACK);
				checkHit = true;
				if(countBees < 100)
				{
					if(countBees % 25 == 0)
					{
						bees.add(new Bee( 345, 25));
						bees.add(new Bee( 545, 25));
						beeBoolean.add(Boolean.FALSE);
						beeBoolean.add(Boolean.FALSE);
						oddEven.add((countBees/25)*2);
						oddEven.add(((countBees/25)*2)+1);
						countStrafeBee.add(0);
						countStrafeBee.add(0);
					}
					countBees++;
				}

				if(bees.size() > 0)
				{
					for(int i = bees.size()-1; i >= 0; i--)
					{
							if(oddEven.get(i) % 2 == 0)
							{
								if(!(beeBoolean.get(i)) && bees.get(i).getY() < 450)
								{
									bees.get(i).setX(bees.get(i).getX() + 5);
									bees.get(i).setY(bees.get(i).getY() + 5);
									iv = new ImageView(bees.get(i).getImage());
									iv.setRotate(135);
									params.setFill(Color.TRANSPARENT);
									rotatedImage = iv.snapshot(params, null);
								}
								else if(!(beeBoolean.get(i)) && bees.get(i).getY() >= 450 && bees.get(i).getY() <= 500)
								{
									bees.get(i).setY(bees.get(i).getY() + 4);
									iv = new ImageView(bees.get(i).getImage());
									iv.setRotate(180);
									params.setFill(Color.TRANSPARENT);
									rotatedImage = iv.snapshot(params, null);
								}
								else if( bees.get(i).getX() >= 730 && bees.get(i).getY() > 500 && bees.get(i).getY() < 540)
								{
									bees.get(i).setX(bees.get(i).getX() - 4);
									bees.get(i).setY(bees.get(i).getY() + 4);
									iv = new ImageView(bees.get(i).getImage());
									iv.setRotate(225);
									params.setFill(Color.TRANSPARENT);
									rotatedImage = iv.snapshot(params, null);
									if(bees.get(i).getY() >= 540)
									{
										beeBoolean.set(i, true);
									}
								}
								else if(beeBoolean.get(i) && bees.get(i).getX() >= 540 && bees.get(i).getY() >= 540)
								{
									bees.get(i).setX(bees.get(i).getX()- 6);
									iv = new ImageView(bees.get(i).getImage());
									iv.setRotate(270);
									params.setFill(Color.TRANSPARENT);
									rotatedImage = iv.snapshot(params, null);
								}
								else if(beeBoolean.get(i) && bees.get(i).getY() > 405 && bees.get(i).getY() < 544 && bees.get(i).getX() >= 407)
								{
									bees.get(i).setX(bees.get(i).getX() - 5);
									bees.get(i).setY(bees.get(i).getY() - 4);
									iv = new ImageView(bees.get(i).getImage());
									iv.setRotate(315);
									params.setFill(Color.TRANSPARENT);
									rotatedImage = iv.snapshot(params, null);
								}
								else if(beeBoolean.get(i) && bees.get(i).getY() < 440 && bees.get(i).getY() >= (180 + (40*(oddEven.get(i)/2))) && bees.get(i).getX() <= 407)
								{
									bees.get(i).setY(bees.get(i).getY() - 6);
									rotatedImage = bees.get(i).getImage();
								}
								else if(bees.get(i).getY() <= (180 + (40*(oddEven.get(i)/2))))
								{
									rotatedImage = bees.get(i).getImage();
									countStrafeBee.set(i, 1);
									startBflyL = true;
								}

						}
						else if(oddEven.get(i) % 2 == 1)
						{
							if(!(beeBoolean.get(i)) && bees.get(i).getY() < 450)
							{
								bees.get(i).setX(bees.get(i).getX() - 5);
								bees.get(i).setY(bees.get(i).getY() + 5);
								iv = new ImageView(bees.get(i).getImage());
								iv.setRotate(225);
								params.setFill(Color.TRANSPARENT);
								rotatedImage = iv.snapshot(params, null);
							}
							else if(!(beeBoolean.get(i)) && bees.get(i).getY() >= 450 && bees.get(i).getY() <= 500)
							{
								bees.get(i).setY(bees.get(i).getY() + 4);
								iv = new ImageView(bees.get(i).getImage());
								iv.setRotate(180);
								params.setFill(Color.TRANSPARENT);
								rotatedImage = iv.snapshot(params, null);
							}
							else if( bees.get(i).getX() <= 215 && bees.get(i).getY() > 500 && bees.get(i).getY() < 540)
							{
								bees.get(i).setX(bees.get(i).getX() + 4);
								bees.get(i).setY(bees.get(i).getY() + 4);
								iv = new ImageView(bees.get(i).getImage());
								iv.setRotate(135);
								params.setFill(Color.TRANSPARENT);
								rotatedImage = iv.snapshot(params, null);
								if(bees.get(i).getY() >= 540)
								{
									beeBoolean.set(i, true);
								}
							}
							else if(beeBoolean.get(i) && bees.get(i).getX() <= 302 && bees.get(i).getY() >= 540)
							{
								bees.get(i).setX(bees.get(i).getX() + 6);
								iv = new ImageView(bees.get(i).getImage());
								iv.setRotate(90);
								params.setFill(Color.TRANSPARENT);
								rotatedImage = iv.snapshot(params, null);
							}
							else if(beeBoolean.get(i) && bees.get(i).getY() > 405 && bees.get(i).getY() < 544 && bees.get(i).getX() >= 302)
							{
								bees.get(i).setX(bees.get(i).getX() + 5);
								bees.get(i).setY(bees.get(i).getY() - 4);
								iv = new ImageView(bees.get(i).getImage());
								iv.setRotate(45);
								params.setFill(Color.TRANSPARENT);
								rotatedImage = iv.snapshot(params, null);
							}
							else if(beeBoolean.get(i) && bees.get(i).getY() < 440 && bees.get(i).getY() >= (180 + (40*(oddEven.get(i)/2))) && bees.get(i).getX() > 450)
							{
								bees.get(i).setY(bees.get(i).getY() - 6);
								rotatedImage = bees.get(i).getImage();
							}
							else if(bees.get(i).getY() <= (180 + (40*(oddEven.get(i)/2))))
							{
								rotatedImage = bees.get(i).getImage();
								countStrafeBee.set(i, 1);
								startBflyL = true;
							}
						}
						gc.drawImage( rotatedImage, bees.get(i).getX(), bees.get(i).getY());
						for(int s = shipShot.size()-1; s >= 0; s--)
						{
							for(int b = bees.size()-1; b >= 0; b--)
							{
								if((shipShot.get(s).getHitBox()).intersects((bees.get(b).getHitBox())))
								{
									dissapearBees(s, b);
									b = 0;
								}
							}
						}

						if(bees.size() > 0)
						{
							int countForStrafe = 0;
							for(int c = bees.size()-1; c >= 0; c--)
							{
								countForStrafe += countStrafeBee.get(c);
							}

							if(countForStrafe ==  countStrafeBee.size())
							{
								strafeBee = true;
							}
						}
					}
				}
				else if(bees.size() == 0)
				{
					startBflyL = true;
				}

				if(startBflyL)
				{
					if(countBflyL < 80)
					{
						if(countBflyL % 10 == 0)
						{
							bfliesL.add(new Butterfly(25, 815));
							bflyOddEvenL.add(countBflyL/10);
							countStrafeBflyL.add(0);
						}
						countBflyL++;
					}

					if(bfliesL.size() > 0)
					{
						for(int i = bfliesL.size()-1; i >= 0; i--)
						{
							if(!strafeBflyL && bfliesL.get(i).getY() > 282 && bfliesL.get(i).getX() < 559)
							{
								bfliesL.get(i).moveTo(750, 225, 6, 45);
								if(bfliesL.get(i).getY() < 282)
									bfliesL.get(i).setY(282);
								//559
								//282
							}
							else if(!strafeBflyL && bflyOddEvenL.get(i) % 2 == 0)
							{
								if(bfliesL.get(i).getY() > (176 + (40*(bflyOddEvenL.get(i)/2))) || ((bflyOddEvenL.get(i) == 6 && bfliesL.get(i).getY() < 296)))
								{
									countStrafeBflyL.set(i, 1);
									bfliesL.get(i).moveTo(559, 176, 6, 0);
									if(bfliesL.get(0).getY() < (176 + (40*(bflyOddEvenL.get(i)/2))))
										bfliesL.get(i).setY((176 + (40*(bflyOddEvenL.get(i)/2))));
									if(bflyOddEvenL.get(i) == 6  && !enemyShooter)
									{
										bfliesL.get(i).setX(559);
										bfliesL.get(i).setY(296);
										bfliesL.get(i).setImage(new Image("Butterfly.png"));
									}
								}
								if(bfliesL.get(0).getY() == (176 + (40*(bflyOddEvenL.get(0)/2))))
								{
									startBflyR = true;
								}
							}
							else if(!strafeBflyL && bflyOddEvenL.get(i) % 2 == 1)
							{
								//x->71, y=176
								if(countStrafeBflyL.get(bfliesL.size()-1) == 0 && bfliesL.get(i).getX() < 642)
								{
									bfliesL.get(i).moveTo(642, 282, 6, 90);
								}

								else if(bfliesL.get(i).getY() > (176 + (40*(bflyOddEvenL.get(i)/2))) || bflyOddEvenL.get(i) == 7)
								{
									countStrafeBflyL.set(i, 1);
									bfliesL.get(i).moveTo(750, 176, 6, 0);
									if(bfliesL.get(i).getY() < (176 + (40*(bflyOddEvenL.get(i)/2))))
									{
										bfliesL.get(i).setY((176 + (40*(bflyOddEvenL.get(i)/2))));
									}
									if(bflyOddEvenL.get(i) == 7 /*&& !enemyShooter*/)
									{
										bfliesL.get(i).setY(296);
										bfliesL.get(i).setImage(new Image("Butterfly.png"));
									}
								}
							}
							if(i < bfliesL.size())
								gc.drawImage(bfliesL.get(i).getImage(), bfliesL.get(i).getX(), bfliesL.get(i).getY());
							for(int s = shipShot.size()-1; s >= 0; s--)
							{
								for(int b = bfliesL.size()-1; b >= 0; b--)
								{
									if((shipShot.get(s).getHitBox()).intersects((bfliesL.get(b).getHitBox())))
									{
										dissapearBflyL(s, b);
										b = 0;
									}
								}
							}

							if(bfliesL.size() > 0)
							{
								int countForStrafe = 0;
								for(int c = bfliesL.size()-1; c >= 0; c--)
								{
									countForStrafe += countStrafeBflyL.get(c);
								}

								if(countForStrafe == countStrafeBflyL.size())
								{
									strafeBflyL = true;
									startBflyR = true;
								}
							}
						}
					}
					else if(bfliesL.size() == 0)
					{
						startBflyR = true;
						strafeBflyL = true;
					}
				}

				if(startBflyR)
				{
					if(countBflyR < 80)
					{
						if(countBflyR % 10 == 0)
						{
							bfliesR.add(new Butterfly(850, 815));
							bflyOddEvenR.add(countBflyR/10);
							countStrafeBflyR.add(0);
						}
						countBflyR++;
					}

					if(bfliesR.size() > 0)
					{
						for(int i = bfliesR.size()-1; i >= 0; i--)
						{
							if(bfliesR.size() > 0 && i < bfliesR.size())
							{
								if(!strafeBflyR && bfliesR.get(i).getY() > 282 && bfliesR.get(i).getX() > 334)
								{
								bfliesR.get(i).moveTo(150, 225, 6, 315);
								if(bfliesR.get(i).getY() < 282)
									bfliesR.get(i).setY(282);
								}
								else if(!strafeBflyR && bflyOddEvenR.get(i) % 2 == 0)
								{
									if(bfliesR.get(i).getY() > (176 + (40*(bflyOddEvenR.get(i)/2))) || ((i == 6 && bfliesR.get(i).getY() < 296)))
									{
										countStrafeBflyR.set(i, 1);
										bfliesR.get(i).moveTo(334, 176, 6, 0);
										if(bfliesR.get(0).getY() < (176 + (40*(bflyOddEvenR.get(i)/2))))
											bfliesR.get(i).setY((176 + (40*(bflyOddEvenR.get(i)/2))));
										if(bflyOddEvenR.get(i) == 6  && !enemyShooter)
										{
											bfliesR.get(i).setX(334);
											bfliesR.get(i).setY(296);
											bfliesR.get(i).setImage(new Image("Butterfly.png"));
										}
										if(bfliesR.size() > 0)
										{
											if(bfliesR.get(0).getY() <= (176 + (40*(bflyOddEvenR.get(0)/2))))
											{
												startBeeT = true;
											}
										}
										else if(bfliesR.size() == 0)
										{
											startBeeT = true;
										}
									}
								}
								else if(!strafeBflyR && bflyOddEvenR.get(i) % 2 == 1)
								{
									//x<-83, y=176
									if(!strafeBflyR && bfliesR.get(i).getX() > 251)
									{
										bfliesR.get(i).moveTo(251, 282, 6, 270);
									}
									else if(bfliesR.get(i).getY() > (176 + (40*(bflyOddEvenR.get(i)/2))) || (i == 7 && bfliesR.get(i).getY() < 296))
									{
										countStrafeBflyR.set(i, 1);
										bfliesR.get(i).moveTo(150, 176, 6, 0);
										if(bfliesR.get(i).getY() < (176 + (40*(bflyOddEvenR.get(i)/2))))
										{
											bfliesR.get(i).setY((176 + (40*(bflyOddEvenR.get(i)/2))));
										}
										if(bflyOddEvenR.get(i) == 7  && !enemyShooter)
										{
											bfliesR.get(i).setY(296);
											bfliesR.get(i).setImage(new Image("Butterfly.png"));
										}
									}
								}
								gc.drawImage(bfliesR.get(i).getImage(), bfliesR.get(i).getX(), bfliesR.get(i).getY());
								for(int s = shipShot.size()-1; s >= 0; s--)
								{
									for(int b = bfliesR.size()-1; b >= 0; b--)
									{
										if((shipShot.get(s).getHitBox()).intersects((bfliesR.get(b).getHitBox())))
										{
											dissapearBflyR(s, b);
											b = 0;
										}
									}
								}

								if(bfliesR.size() > 0)
								{
									int countForStrafe = 0;
									for(int c = bfliesR.size()-1; c >= 0; c--)
									{
										countForStrafe += countStrafeBflyR.get(c);
									}

									if(countForStrafe == countStrafeBflyR.size())
									{
										strafeBflyR = true;
										startBeeT = true;
									}
								}
							}
						}
					}
					else if(bfliesR.size() == 0)
					{
						startBeeT = true;
						strafeBflyR = true;
					}
				}

				if(startBeeT)
				{
					//1: 251
					//2: 334
					//3: 403
					//4: 479
					//5: 559
					//6: 642
					if(countBeeT < 1)
					{
						for(int i = 0; i < 6; i++)
						{
							if(i == 0)
								beeT.add(new Bee(251, 25));
							if(i == 1)
								beeT.add(new Bee(334, 25));
							if(i == 2)
								beeT.add(new Bee(403, 25));
							if(i == 3)
								beeT.add(new Bee(479, 25));
							if(i == 4)
								beeT.add(new Bee(559, 25));
							if(i == 5)
								beeT.add(new Bee(642, 25));
							 countStrafeBeeT.add(0);
							 limitTL.add(0);
							 limitTR.add(0);
							 beeTNum.add(i);
						}
						countBeeT++;
					}

					if(beeT.size() > 0)
					{
						for(int i = beeT.size()-1; i >= 0; i--)
						{
							if(i < beeT.size())
							{
								if(beeT.get(i).getY() < 116)
									beeT.get(i).moveTo(beeT.get(i).getX(), 116, 4, 180);
								if(beeT.get(0).getY() >= 116 && (rotateBeeT < 6 || (rotateBeeT >= 6 && rotateBeeT < 12)))
								{
									beeT.get(i).setY(116);
									limitTL.set(i, beeT.get(i).getX()+176);
									limitTR.set(i, beeT.get(i).getX()-207);
									ImageView iv = new ImageView(new Image("bee.png"));
									iv.setRotate(90*(rotateBeeT/6+3));
									SnapshotParameters params = new SnapshotParameters();
									params.setFill(Color.TRANSPARENT);
									beeT.get(i).setImage(iv.snapshot(params, null));
									rotateBeeT++;
									if(rotateBeeT/6 == 1)
									{
										countStrafeBeeT.set(i, 1);
									}
								}
								if(beeT.size() > 0)
								{
									gc.drawImage(beeT.get(i).getImage(), beeT.get(i).getX(), beeT.get(i).getY());
									for(int s = shipShot.size()-1; s >= 0; s--)
									{
										for(int b = beeT.size()-1; b >= 0; b--)
										{
											if((shipShot.get(s).getHitBox()).intersects((beeT.get(b).getHitBox())))
											{
												dissapearBeeT(s, b);
												b = 0;
											}
										}
									}

									if(beeT.size() > 0)
									{
										int countForStrafe = 0;
										for(int c = beeT.size()-1; c >= 0; c--)
										{
											countForStrafe += countStrafeBeeT.get(c);
										}

										if(countForStrafe == countStrafeBeeT.size())
										{
											strafeBeeT = true;
										}
									}
								}
							}
						}
					}
				}

				if(strafeBee && strafeBflyL && strafeBflyR && strafeBeeT)
				{
					//
					//
					//
					if(!enemyShooter)
					{
						int enemyType = (int)(Math.random()*2)+1;
						if(enemyType == 1 && bfliesL.size() > 0)
						{
							enemyIndex = bflyOddEvenL.get((int)(Math.random()*bfliesL.size()));
							for(int i = bfliesL.size()-1; i >= 0; i--)
							{
								if(bflyOddEvenL.get(i) == enemyIndex)
								{
									xOrig = bfliesL.get(i).getX();
									yOrig = bfliesL.get(i).getY();
								}
							}
							enemyBL = true;
							enemyShooter = true;
						}
						else if(enemyType == 2 && bfliesR.size() > 0)
						{
							enemyIndex = bflyOddEvenR.get((int)(Math.random()*bfliesR.size()));
							for(int i = bfliesR.size()-1; i >= 0; i--)
							{
								if(bflyOddEvenR.get(i) == enemyIndex)
								{
									xOrig = bfliesR.get(i).getX();
									yOrig = bfliesR.get(i).getY();
								}
							}
							enemyBR = true;
							enemyShooter = true;
						}
					}

					if(enemyShooter)
					{
						if(enemyBL)
						{
							for(int i = bfliesL.size()-1; i >= 0; i--)
							{
								if(bflyOddEvenL.get(i) == enemyIndex)
								{
									if(i < bfliesL.size() && !offScreen && !comeBack)
									{
										if(bfliesL.get(i).getY() < 520)
										{
											bfliesL.get(i).moveTo(bfliesL.get(i).getX(), 520, 9, 180);
										}
										else if(bfliesL.get(i).getX() >= 0)
										{
											bfliesL.get(i).moveTo(-10, bfliesL.get(i).getY(), 8, 270);
											if((int)(Math.random()*25)+1 == 1)
											{
												spriteShot.add(0, new SpriteShot(bfliesL.get(i).getX(), bfliesL.get(i).getY()));
												for(int s = 0; s < spriteShot.size(); s++)
												{
													gc.drawImage( spriteShot.get(s).getImage(), spriteShot.get(s).getX(), spriteShot.get(s).getY());
												}
											}
										}
										if(bfliesL.get(i).getY() > 920 || bfliesL.get(i).getY() < 0 || bfliesL.get(i).getX() > 925 || bfliesL.get(i).getX() < 0)
											offScreen = true;
									}
									else if(i < bfliesL.size() && offScreen)
									{
										bfliesL.get(i).setX(xOrig);
										bfliesL.get(i).setY(0);
										comeBack = true;
										offScreen = false;
									}
									else if(i < bfliesL.size() && comeBack)
									{

										if(bfliesL.get(i).getY() < yOrig)
										{
											bfliesL.get(i).moveTo(bfliesL.get(i).getX(), yOrig, 10, 180);
										}
										if(bfliesL.get(i).getY() == yOrig && !(bfliesL.get(i).getX() == xOrig))
										{
											if(bfliesL.get(i).getX() > xOrig)
											{
												bfliesL.get(i).moveTo(xOrig, bfliesL.get(i).getY(), 8, 270);
											}
											if(bfliesL.get(i).getX() < xOrig)
											{
												bfliesL.get(i).moveTo(xOrig, bfliesL.get(i).getY(), 8, 90);
											}
										}
										if(bfliesL.get(i).getY() == yOrig && bfliesL.get(i).getX() == xOrig)
										{
											bfliesL.get(i).setImage(new Image("butterfly.png"));
											comeBack = false;
											enemyBL = false;
											enemyShooter = false;
										}
									}
								}
							}
						}
						else if(enemyBR)
						{
							for(int i = bfliesR.size()-1; i >= 0; i--)
							{
								if(bflyOddEvenR.get(i) == enemyIndex)
								{
									if(i < bfliesR.size() && !offScreen && !comeBack)
									{
										if(bfliesR.get(i).getY() < 520)
										{
											bfliesR.get(i).moveTo(bfliesR.get(i).getX(), 520, 9, 180);
										}
										else if(bfliesR.get(i).getX() >= 0)
										{
											bfliesR.get(i).moveTo(935, bfliesR.get(i).getY(), 8, 90);
											if((int)(Math.random()*25)+1 == 1)
											{
												spriteShot.add(0, new SpriteShot(bfliesR.get(i).getX(), bfliesR.get(i).getY()));
												for(int s = 0; s < spriteShot.size(); s++)
												{
													gc.drawImage( spriteShot.get(s).getImage(), spriteShot.get(s).getX(), spriteShot.get(s).getY());
												}
											}
										}
										if(bfliesR.get(i).getY() > 920 || bfliesR.get(i).getY() < 0 || bfliesR.get(i).getX() > 925 || bfliesR.get(i).getX() < 0)
											offScreen = true;
									}
									else if(i < bfliesR.size() && offScreen)
									{
										bfliesR.get(i).setX(xOrig);
										bfliesR.get(i).setY(0);
										comeBack = true;
										offScreen = false;
									}
									else if(i < bfliesR.size() && comeBack)
									{

										if(bfliesR.get(i).getY() < yOrig)
										{
											bfliesR.get(i).moveTo(bfliesR.get(i).getX(), yOrig, 10, 180);
										}
										if(bfliesR.get(i).getY() == yOrig && !(bfliesR.get(i).getX() == xOrig))
										{
											if(bfliesR.get(i).getX() > xOrig)
											{
												bfliesR.get(i).moveTo(xOrig, bfliesR.get(i).getY(), 8, 270);
											}
											if(bfliesR.get(i).getX() < xOrig)
											{
												bfliesR.get(i).moveTo(xOrig, bfliesR.get(i).getY(), 8, 90);
											}
										}
										if(bfliesR.get(i).getY() == yOrig && bfliesR.get(i).getX() == xOrig)
										{
											bfliesR.get(i).setImage(new Image("butterfly.png"));
											comeBack = false;
											enemyBR = false;
											enemyShooter = false;
										}
									}
								}
							}
						}
					}


					if(strafeR)
					{
						xOrig+=3;
						for(int i = 0; i < bees.size(); i++)
						{
							if(bees.size() > 0)
							{
								int check = bees.size()-1;
								int limit;
								if(oddEven.get(check) % 2 == 0)
									limit = 580;
								else
									limit = 655;
								bees.get(i).setX(bees.get(i).getX() + 3);
								if(bees.get(i).getX() > 580 && oddEven.get(i) % 2 == 0)
									bees.get(i).setX(580);
								if(bees.get(i).getX() > 655 && oddEven.get(i) % 2 == 1)
									bees.get(i).setX(655);
								if(bees.get(check).getX() == limit)
								{
									strafeR = false;
									strafeL = true;
								}
							}
						}
						for(int i = 0; i < bfliesL.size(); i++)
						{
							//bflyL: 559, 642
							if(bfliesL.size() > 0 && !( enemyBL && bflyOddEvenL.get(i) == enemyIndex))
							{
								int check = bfliesL.size()-1;
								int limit;
								if(bflyOddEvenL.get(check) % 2 == 0)
									limit = 736;
								else
									limit = 818;
								bfliesL.get(i).setX(bfliesL.get(i).getX() + 3);
								if(bfliesL.get(i).getX() > 736 && bflyOddEvenL.get(i) % 2 == 0)
									bfliesL.get(i).setX(736);
								if(bfliesL.get(i).getX() > 818 && bflyOddEvenL.get(i) % 2 == 1)
									bfliesL.get(i).setX(818);
								if(bfliesL.get(check).getX() == limit)
								{
									strafeR = false;
									strafeL = true;
								}
							}
						}
						for(int i = 0; i < bfliesR.size(); i++)
						{
							if(bfliesR.size() > 0 && !( enemyBR && bflyOddEvenR.get(i) == enemyIndex))
							{
								int check = bfliesR.size()-1;
								int limit;
								if(bflyOddEvenR.get(check) % 2 == 0)
									limit = 510;
								else
									limit = 428;
								bfliesR.get(i).setX(bfliesR.get(i).getX() + 3);
								if(bfliesR.get(i).getX() > 510 && bflyOddEvenR.get(i) % 2 == 0)
									bfliesR.get(i).setX(510);
								if(bfliesR.get(i).getX() > 428 && bflyOddEvenR.get(i) % 2 == 1)
									bfliesR.get(i).setX(428);
								if(bfliesR.get(check).getX() == limit)
								{
									strafeR = false;
									strafeL = true;
								}
							}
						}
						int check = 0;
						int limitT = 0;
						if(beeT.size() > 0)
							limitT = limitTL.get(0);
						for(int i = 0; i < beeT.size(); i++)
						{
							if(beeT.size() > 0)
							{
								beeT.get(i).setX(beeT.get(i).getX() + 3);
								if(beeT.get(check).getX() >= limitT)
								{
									strafeR = false;
									strafeL = true;
								}
							}
						}
					}
					else if(strafeL)
					{
						xOrig-=3;
						for(int i = 0; i < bees.size(); i++)
						{
							if(bees.size() > 0)
							{
								int check = bees.size()-1;
								int limit;
								if(oddEven.get(check) % 2 == 0)
									limit = 195;
								else
									limit = 272;
								bees.get(i).setX(bees.get(i).getX() - 3);
								if(bees.get(i).getX() < 195 && oddEven.get(i) % 2 == 0)
									bees.get(i).setX(195);
								if(bees.get(i).getX() < 272 && oddEven.get(i) % 2 == 1)
									bees.get(i).setX(272);
								if(bees.get(check).getX() == limit)
								{
									strafeR = true;
									strafeL = false;
								}
							}
						}
						for(int i = 0; i < bfliesL.size(); i++)
						{
							if(bfliesL.size() > 0 && !( enemyBL && bflyOddEvenL.get(i) == enemyIndex))
							{
								int check = bfliesL.size()-1;
								int limit;
								if(bflyOddEvenL.get(check) % 2 == 0)
									limit = 351;
								else
									limit = 435;
								bfliesL.get(i).setX(bfliesL.get(i).getX() - 3);
								if(bfliesL.get(i).getX() < 351 && bflyOddEvenL.get(i) % 2 == 0)
									bfliesL.get(i).setX(351);
								if(bfliesL.get(i).getX() < 435 && bflyOddEvenL.get(i) % 2 == 1)
									bfliesL.get(i).setX(435);
								if(bfliesL.get(check).getX() == limit)
								{
									strafeR = true;
									strafeL = false;
								}
							}
						}
						for(int i = 0; i < bfliesR.size(); i++)
						{
							//bflyR: 251, 334
							if(bfliesR.size() > 0 && !( enemyBR && bflyOddEvenR.get(i) == enemyIndex))
							{
								int check = bfliesR.size()-1;
								int limit;
								if(bflyOddEvenR.get(check) % 2 == 0)
									limit = 127;
								else
									limit = 43;
								bfliesR.get(i).setX(bfliesR.get(i).getX() - 3);
								if(bfliesR.get(i).getX() < 127 && bflyOddEvenR.get(i) % 2 == 0)
									bfliesR.get(i).setX(127);
								if(bfliesR.get(i).getX() < 43 && bflyOddEvenR.get(i) % 2 == 1)
									bfliesR.get(i).setX(43);
								if(bfliesR.get(check).getX() == limit)
								{
									strafeR = true;
									strafeL = false;
								}
							}
						}
						int check = 0;
						int limitT = 0;
						if(beeT.size() > 0)
							limitT = limitTR.get(0);
						for(int i = 0; i < beeT.size(); i++)
						{
							if(beeT.size() > 0)
							{
								beeT.get(i).setX(beeT.get(i).getX() - 3);
								if(beeT.get(check).getX() <= limitT)
								{
									strafeR = true;
									strafeL = false;
								}
							}
						}
					}
				}
				gc.drawImage(spaceship, 425+shipX, 795);

				for(int s = 0; s < spriteShot.size(); s++)
				{
					spriteShot.get(s).setY(spriteShot.get(s).getY() + 8);
					gc.drawImage( spriteShot.get(s).getImage(), spriteShot.get(s).getX(), spriteShot.get(s).getY());
					if(spriteShot.get(s).getHitBox().intersects(control.getHitBox()))
					{
						clip2.play();
						spriteShot.remove(s);
						end = true;
					}
				}


				for(int i = 0; i < shipShot.size(); i++)
				{
					shipShot.get(i).setY(shipShot.get(i).getY() - 7);
					gc.drawImage( shipShot.get(i).getImage(), shipShot.get(i).getX(), shipShot.get(i).getY());
				}

				if(shotTimer == 0 || timer > shotTimer + .5)
				{
					if(createShot)
					{
						shipShot.add(0, new ShipShot(shotStart, 774));
						shotTimer = timer;
						for(int i = 0; i < shipShot.size(); i++)
						{
							gc.drawImage( shipShot.get(i).getImage(), shipShot.get(i).getX(), shipShot.get(i).getY());
							createShot = false;
						}
					}
				}
				createShot = false;

				if(moveLeft && shipX > -375)
				{
					moveRight = false;
					shipX-=10;
					control.setX(control.getX()-10);
				}
				if(moveRight && shipX < 400)
				{
					moveLeft = false;
					shipX+=10;
					control.setX(control.getX()+10);
				}
				if(score == 3000)
				{
					gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					gc.setFill(Color.BLACK);
					gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					gc.setFill(Color.WHITE);
					gc.setStroke(Color.WHITE ); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont( font );
					gc.fillText("Winner!", 370, 440 ); //draws the yellow part of the text
					gc.strokeText("Winner!", 370, 440 ); //draws the outline part of the text
					gc.setFill(Color.BLACK);
				}
				if(end)
				{
					gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					gc.setFill(Color.BLACK);
					gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					gc.setFill(Color.WHITE);
					gc.setStroke(Color.WHITE ); //Changes the outline the black
					gc.setLineWidth(1); //How big the black lines will be
					font = Font.font( "Arial", FontWeight.NORMAL, 48 );
					gc.setFont( font );
					gc.fillText("Trash!",390, 440 ); //draws the yellow part of the text
					gc.strokeText("Trash!", 390, 440 ); //draws the outline part of the text
					gc.setFill(Color.BLACK);
				}
			}
		}
	}

	public void dissapearBees(int s, int b)
	{
		shipShot.remove(s);
		bees.remove(b);
		beeBoolean.remove(b);
		oddEven.remove(b);
		countStrafeBee.remove(b);
		score += 100;
		clip.play();
	}


	public void dissapearBflyL(int s, int b)
	{
		if(enemyBL && bflyOddEvenL.get(b) == enemyIndex && enemyShooter)
		{
			comeBack = false;
			offScreen = false;
			enemyBL = false;
			enemyShooter = false;
		}
		shipShot.remove(s);
		bfliesL.remove(b);
		bflyOddEvenL.remove(b);
		countStrafeBflyL.remove(b);
		score += 100;
		clip.play();
	}

	public void dissapearBflyR(int s, int b)
	{
		if(enemyBR && bflyOddEvenR.get(b) == enemyIndex && enemyShooter)
		{
			comeBack = false;
			offScreen = false;
			enemyBL = false;
			enemyShooter = false;
		}
		shipShot.remove(s);
		bfliesR.remove(b);
		bflyOddEvenR.remove(b);
		countStrafeBflyR.remove(b);
		score += 100;
		clip.play();
	}

	public void dissapearBeeT(int s, int b)
	{
		shipShot.remove(s);
		beeT.remove(b);
		countStrafeBeeT.remove(b);
		beeTNum.remove(b);
		score += 100;
		clip.play();
		limitTL.remove(b);
		limitTR.remove(b);
	}

	public void handle(final InputEvent event)
	{
		if (event instanceof KeyEvent)
		{
			if(((KeyEvent)event).getCode() == KeyCode.LEFT && shipX > -375)
			{
				if(event.getEventType().toString().equals("KEY_PRESSED"))
					moveLeft = true;
				if(event.getEventType().toString().equals("KEY_RELEASED") )
					moveLeft = false;
			}
			if(((KeyEvent)event).getCode() == KeyCode.RIGHT && shipX < 400)
			{
				if(event.getEventType().toString().equals("KEY_PRESSED"))
					moveRight = true;
				if(event.getEventType().toString().equals("KEY_RELEASED") )
					moveRight = false;
			}
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

			if(((KeyEvent)event).getCode() == KeyCode.SPACE)
			{
				if(event.getEventType().toString().equals("KEY_RELEASED"))
				{
					startGame = true;
					createShot = true;
					shotStart = 446 + shipX;
				}
			}
			if(((KeyEvent)event).getCode() == KeyCode.R)
			{
				if(event.getEventType().toString().equals("KEY_PRESSED"))
				{
					resource = getClass().getResource("blop.wav");
					clip = new AudioClip(resource.toString());
					resource2 = getClass().getResource("metal.wav");
					clip2 = new AudioClip(resource.toString());

					score = 0;
					end = false;

					limitTL = new ArrayList<Integer>();
					limitTR = new ArrayList<Integer>();

					startGame = false;
					checkHit = false;

					createShot = false;
					shotStart = 0;
					shipShot = new ArrayList<ShipShot>();

					spriteShot = new ArrayList<SpriteShot>();

					startBflyL = false;
					startBflyR = false;
					startBeeT = false;

					bees = new ArrayList<Bee>();
					oddEven = new ArrayList<Integer>();
					beeBoolean = new ArrayList<Boolean>();
					countBees = 0;

					countBflyL = 0;
					bfliesL = new ArrayList<Butterfly>();
					bflyOddEvenL = new ArrayList<Integer>();
					countBflyR = 0;
					bfliesR = new ArrayList<Butterfly>();
					bflyOddEvenR = new ArrayList<Integer>();

					countBeeT = 0;
					rotateBeeT = 0;
					beeT = new ArrayList<Bee>();
					beeTNum = new ArrayList<Integer>();

					countStrafeBee = new ArrayList<Integer>();
					countStrafeBflyL = new ArrayList<Integer>();
					countStrafeBflyR = new ArrayList<Integer>();
					countStrafeBeeT = new ArrayList<Integer>();
					strafeBee = false;
					strafeBflyL = false;
					strafeBflyR = false;
					strafeBeeT= false;
					strafeL = false;
					strafeR = true;

					boolean enemyShooter = false;
					boolean enemyBL = false;
					boolean enemyBR = false;
					boolean enemyBT = false;

					timer = 0;
					shotTimer = 0;
				}
			}
		}
	}
}