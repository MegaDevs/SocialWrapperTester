package com.megadevs.socialwrappertester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.megadevs.socialwrapper.SocialFriend;
import com.megadevs.socialwrapper.SocialNetwork;
import com.megadevs.socialwrapper.SocialWrapper;
import com.megadevs.socialwrapper.exceptions.InvalidSocialRequestException;
import com.megadevs.socialwrapper.exceptions.SocialNetworkNotFoundException;
import com.megadevs.socialwrapper.thefacebook.TheFacebook;
import com.megadevs.socialwrapper.thefacebook.TheFacebook.TheFacebookPictureCallback;
import com.megadevs.socialwrapper.theflickr.TheFlickr;
import com.megadevs.socialwrapper.thefoursquare.TheFoursquare;
import com.megadevs.socialwrapper.thefoursquare.TheFoursquareVenue;
import com.megadevs.socialwrapper.thetumblr.TheTumblr;
import com.megadevs.socialwrapper.thetwitter.TheTwitter;

public class SocialWrapperTester extends ListActivity {

	TheFacebook fb;
	TheTwitter tw;
	TheFoursquare fs;
	TheFlickr fl;
	TheTumblr tu;

	public static String checkinID;
	
	private static String tag = "SocialWrapper";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		final SocialWrapper s = SocialWrapper.getInstance();
		s.setActivity(SocialWrapperTester.this);

		// ----------------------------------------------
		// ----------------- TWITTER --------------------
		// ----------------------------------------------

		try {
			tw = (TheTwitter) s.getSocialNetwork(SocialWrapper.THETWITTER);
		} catch (SocialNetworkNotFoundException e) {
			Log.i("corso", e.getMessage());
		}
 
		Button authTwitter = new Button(this);
		authTwitter.setText("Twitter : auth");
//		authTwitter.setOnClickListener (new OnClickListener() {
//			public void onClick(View v) {
//				Log.i(tag, "Auth Twitter!!!!");
//				try {
//					tw.authenticate();
//				} catch (InvalidAuthenticationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}}); 

		Button tweet = new Button(this);
		tweet.setText("Twitter : tweet");
		tweet.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "Tweet!!!!");
				tw.selfPost("Ciao amici, stiamo testando!!!", null);
			}});

		Button tweetToFriend = new Button(this);
		tweetToFriend.setText("Twitter : tweet to friend");
		tweetToFriend.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "Tweet to Friend!!!!");
				tw.postToFriend("megadevs", "ciao stronzi", null);
			}});

		Button getFriendsTwitter = new Button(this);
		getFriendsTwitter.setText("Twitter : friends list");
		getFriendsTwitter.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "getFriendsTwitter!!!!");
//				try {
//					printFriendsList(tw.getFriendsList());
//				} catch (InvalidSocialRequestException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}});

		// ----------------------------------------------
		// ----------------- FACEBOOK -------------------
		// ----------------------------------------------

		try {
			fb = (TheFacebook) s.getSocialNetwork(SocialWrapper.THEFACEBOOK);
			fb.init("210912465637288");
		} catch (SocialNetworkNotFoundException e) {
			Log.i("corso", e.getMessage());
		}
		


		Button authFacebook = new Button(this);
		authFacebook.setText("Facebook : auth");
		authFacebook.setOnClickListener (new OnClickListener() {
			public void onClick(View v) { 
				Log.i(tag, "authFacebook!!!!");
				fb.authenticate(new TheFacebook.TheFacebookLoginCallback() {

					@Override
					public void onLoginCallback(String result) {
						if (result == SocialNetwork.ACTION_SUCCESSFUL)
							Toast.makeText(SocialWrapperTester.this, "Facebook successfully authenticated", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(SocialWrapperTester.this, "Facebook cannot be authenticated", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onErrorCallback(String error, Exception e) {
						Toast.makeText(SocialWrapperTester.this, error, Toast.LENGTH_LONG).show();
					}  
				});
			}});

		Button postFacebook = new Button(this);
		postFacebook.setText("Facebook : post to my wall");
		postFacebook.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "postFacebook!!!!");
				fb.postOnMyWall("post on my wall test","","","","", new TheFacebook.TheFacebookPostCallback() {
					@Override
					public void onPostCallback(String result) {
						if (result == SocialNetwork.ACTION_SUCCESSFUL)
							System.out.println("message posted");
						else
							System.out.println("message not posted");
					}
					
					@Override
					public void onErrorCallback(String error, Exception e) {
						Toast.makeText(SocialWrapperTester.this, error, Toast.LENGTH_LONG).show();
					}
				});
			}});

		Button postToFriendFacebook = new Button(this);
		postToFriendFacebook.setText("Facebook : post to friend's wall");
		postToFriendFacebook.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "postToFriend!!!!");
				try {
					fb.postToFriendsWall("dario.marcato", "post to friend's wall test","","","","", new TheFacebook.TheFacebookPostCallback() {
						@Override
						public void onPostCallback(String result) {
							if (result == SocialNetwork.ACTION_SUCCESSFUL)
								System.out.println("message posted");
							else
								System.out.println("message not posted");
						}
						
						@Override
						public void onErrorCallback(String error, Exception e) {
							Toast.makeText(SocialWrapperTester.this, error, Toast.LENGTH_LONG).show();
						}
					});
				} catch (InvalidSocialRequestException e) {
					e.printStackTrace();
				}
			}
		});
							
		Button getFriendsFacebook = new Button(this);
		getFriendsFacebook.setText("Facebook : get friends list");
		getFriendsFacebook.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "getFriendsFacebook!!!!");
				fb.getFriendsList(new TheFacebook.TheFacebookFriendListCallback() {
					@Override
					public void onFriendsListCallback(String result, ArrayList<SocialFriend> list) {
						if (result == SocialNetwork.ACTION_SUCCESSFUL && list.size() != 0)
							printFriendsList(list);
						else
							System.out.println("friends list FAILED");
					}
					
					@Override
					public void onErrorCallback(String error, Exception e) {
						Toast.makeText(SocialWrapperTester.this, error, Toast.LENGTH_LONG).show();
					}
				});
			}});

		Button postPictureFacebook = new Button(this);
		postPictureFacebook.setText("Facebook : post picture");
		postPictureFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				File f = new File("/sdcard/test2.jpg");
//				File f = new File("/sdcard/test3.png");
//				FileInputStream is;
//				byte[] data = new byte[(int) f.length()];
//				try {
//					is = new FileInputStream(f);
//					is.read(data);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				
				int buffSize = 1024;
				
				try {
					URL url = new URL("http://i0.kym-cdn.com/entries/icons/original/000/000/005/pedobear.jpg");
					URLConnection conn = url.openConnection();
					InputStream is = conn.getInputStream();
					
					byte[] buf = new byte[buffSize];
					Vector<byte[]> v = new Vector<byte[]>();
					
					while (is.read(buf) != -1)
						v.add(buf);
					
					is.close();
					
					int imgSize = (v.size()-1) * buffSize + v.lastElement().length;
					System.out.println(imgSize);
					byte[] image = new byte[imgSize];
					
					for (int i=0; i<v.size(); i++) {
						byte[] current = v.get(i);
						for (int j=0; j<current.length; j++)
							image[i*buffSize+j] = current[j];
					}
					
					byte[] data = image;

					fb.postPicture(data, "sebinotest", new TheFacebookPictureCallback() {
						
						@Override
						public void onPostPictureCallback(String result) {
							if (result == SocialNetwork.ACTION_SUCCESSFUL) {
								System.out.println("picture posted");
							} else {
								System.out.println("picture not posted");
							}
						}
						
						@Override
						public void onErrorCallback(String error, Exception e) {
							Toast.makeText(SocialWrapperTester.this, error, Toast.LENGTH_LONG).show();
						}
					});
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		Button logoutFacebook = new Button(this);
		logoutFacebook.setText("Facebook : logout");
		logoutFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i(tag, "logout facebook");
				fb.deauthenticate();
			}
		});
		
		// ----------------------------------------------
		// ---------------- FOURSQUARE ------------------
		// ----------------------------------------------

		try {
			fs = (TheFoursquare) s.getSocialNetwork(SocialWrapper.THEFOURSQUARE);
		} catch (SocialNetworkNotFoundException e) {
			Log.i("corso", e.getMessage());
		}
		fs.setAuthParams("AWL4NZYKTOTOBSM4H5B41FLHJFRXADW4MU3CNMTVV4EEUJW3", "SQPMNWNCF4BD5NP1SUWZWWHVB4U35CLOJ1FGJBTIN3UE5X4H", "fsapitest://connect");

		Button authFoursquare = new Button(this);
		authFoursquare.setText("Foursquare : auth");
		authFoursquare.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "authFoursquare!!!!");
				fs.authenticate(new TheFoursquare.TheFoursquareLoginCallback() {
					@Override
					public void onLoginCallback(String result) {
						System.out.println("foursquare authenticated");
					}
					
					@Override
					public void onErrorCallback(String error, Exception e) {
						System.out.println("foursquare authentication error");
					}
				});
			}
		});

		Button searchFoursquare = new Button(this);
		searchFoursquare.setText("Foursquare : search");
		searchFoursquare.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "searchFoursquare!!!!");
				try {
					ArrayList<TheFoursquareVenue> venues = fs.searchVenues(44, 44, null);
					
					for (TheFoursquareVenue venue : venues) {
						Log.i("corso", venue.getVenueID() + " -- " + venue.getVenueName());
					}
				} catch (InvalidSocialRequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});

		Button getFriendsFoursquare = new Button(this);
		getFriendsFoursquare.setText("Foursquare : friends list");
		getFriendsFoursquare.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				Log.i(tag, "getFriendsFoursquare!!!!");
//				try {
//					printFriendsList(fs.getFriendsList());
//				} catch (InvalidSocialRequestException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		}});
		
		Button checkInFoursquare = new Button(this);
		checkInFoursquare.setText("Foursquare : checkin");
		checkInFoursquare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(tag, "checkin foursquare");
				checkinID = null;
				//String paolotti = "4c31a4b1452620a152e4200f";
				String luzzatti = "4c31cb31452620a19b0c210f";

				fs.checkIn(luzzatti, "this is a test", new TheFoursquare.TheFoursquareCheckinCallback() {
					@Override
					public void onErrorCallback(String error, Exception e) {
						Toast.makeText(SocialWrapperTester.this, "Checkin failed", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCheckinCallback(String result, String checkinId) {
						checkinID = checkinId;
						Toast.makeText(SocialWrapperTester.this, "Checked in", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

		Button postPictureFoursquare = new Button(this); 
		postPictureFoursquare.setText("Foursquare : post picture");
		postPictureFoursquare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(tag, "post picture foursquare");
				File f = new File("/sdcard/test2.jpg");
				if (f.exists()) {
					fs.postPicture(f, checkinID, new TheFoursquare.TheFoursquarePostPictureCallback() {
						@Override
						public void onPostPictureCallback(String result) {
							Toast.makeText(SocialWrapperTester.this, "Picture posted", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onErrorCallback(String error, Exception e) {
							Toast.makeText(SocialWrapperTester.this, "Picture not posted", Toast.LENGTH_SHORT).show();
						}
					});
				}
				else
					Toast.makeText(SocialWrapperTester.this, "Picture does not exists", Toast.LENGTH_SHORT).show();
			}
		});
		
		// ----------------------------------------------
		// ---------------- FLICKR ------------------
		// ----------------------------------------------
		
		try {
			fl = (TheFlickr) s.getSocialNetwork(SocialWrapper.THEFLICKR);
		} catch (SocialNetworkNotFoundException e) {
			e.printStackTrace();
		}
		fl.setAuthParams("c4f9b3296fc863136c93e8e7b6df85c1", "3a764ccb63cdfc1b", "flickr://oauth");
		
		Button authFlickr = new Button(this);
		authFlickr.setText("Flickr : auth");
		authFlickr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fl.authenticate(new TheFlickr.TheFlickrLoginCallback() {
					@Override
					public void onLoginCallback(String result) {
						System.out.println("flickr login callback");
						if (result == SocialNetwork.ACTION_SUCCESSFUL)
							Toast.makeText(SocialWrapperTester.this, "Flickr successfully authenticated", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onErrorCallback(String error, Exception e) {
						System.out.println("flickr auth error");
						Toast.makeText(SocialWrapperTester.this, "Flickr authentication error", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		
		Button uploadFlickr = new Button(this);
		uploadFlickr.setText("Flickr : upload");
		uploadFlickr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File f = new File("/sdcard/test2.jpg");
				InputStream s;
				try {
					s = new FileInputStream(f);
					
					byte[] image = new byte[(int) f.length()];
					while (s.available() > 0)
						s.read(image);
					
					System.out.println("lenght = " + image.length);
					
					fl.upload(image, "diomerda/ziochen", new TheFlickr.TheFlickrPostPictureCallback() {
						
						@Override
						public void onPostPictureCallback(String result) {
							System.out.println("posted!");
						}
						
						@Override
						public void onErrorCallback(String error, Exception e) {
							System.out.println("error! " + error);
							if (e != null)
								e.printStackTrace();
						}
					});

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// ----------------------------------------------
		// ---------------- TUMBLR ------------------
		// ----------------------------------------------

		try {
			tu = (TheTumblr) s.getSocialNetwork(SocialWrapper.THETUMBLR);
		} catch (SocialNetworkNotFoundException e) {
			e.printStackTrace();
		}

		tu.setParameters("vnIBtBEf0VGdBYKlsk0LsMsKYsLGazq0AaK1zOuRjPni75SIL5", "jSHDiUUU3BRPeCIaSv6t6dQKCpwsJM1Y1H4Iv07OhiyYx0zRuJ", "oauthflow-tumblr://callback");
		
		Button authTumblr = new Button(this);
		authTumblr.setText("Tumblr : auth");
		authTumblr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tu.authenticate(new TheTumblr.TheTumblrLoginCallback() {
					@Override
					public void onLoginCallback(String result) {
						System.out.println("tumblr successfully authenticated");
					}
					
					@Override
					public void onErrorCallback(String error, Exception e) {
						System.out.println("tumblr authentication error");
						e.printStackTrace();
					}
				});
			}
		});
		
		Button postPictureTumblr = new Button(this);
		postPictureTumblr.setText("Tumblr : post picture");
		postPictureTumblr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				File f = new File("/sdcard/test2.jpg");
				InputStream s;
				try {
					s = new FileInputStream(f);
					byte[] image = new byte[(int) f.length()];
					while (s.available() > 0)
						s.read(image);

					System.out.println("lenght = " + image.length);

					tu.uploadImage(image, "", new TheTumblr.TheTumblrPostPictureCallback() {
						@Override
						public void onPostPictureCallback(String result) {
							System.out.println("image uploaded!");
						}
						
						@Override
						public void onErrorCallback(String error, Exception e) {
							System.out.println("image upload error");
						}
					});

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		Button close = new Button(this);
		close.setText("Close");
		close.setOnClickListener (new OnClickListener() {
			public void onClick(View v) {
				finish();
				System.exit(0);}});

		Button[] buttonList = new Button[20];
		buttonList[0]  = authFacebook;
		buttonList[1]  = postFacebook;
		buttonList[2]  = postToFriendFacebook;
		buttonList[3]  = postPictureFacebook;
		buttonList[4]  = getFriendsFacebook;
		buttonList[5]  = logoutFacebook;
		buttonList[6]  = authTwitter;
		buttonList[7]  = tweet;
		buttonList[8]  = tweetToFriend;
		buttonList[9]  = getFriendsTwitter;
		buttonList[10] = authFoursquare;
		buttonList[11] = searchFoursquare;
		buttonList[12] = getFriendsFoursquare;
		buttonList[13] = postPictureFoursquare;
		buttonList[14] = checkInFoursquare;
		buttonList[15] = authFlickr;
		buttonList[16] = uploadFlickr;
		buttonList[17] = authTumblr;
		buttonList[18] = postPictureTumblr;
		buttonList[19] = close;

		setListAdapter(new ItemsAdapter(this, R.layout.item, buttonList));
	}

	private void printFriendsList(ArrayList<SocialFriend> friendList) {
		if (friendList != null)
			for(SocialFriend f : friendList) {
				Log.i(tag, f.getName() + " - " + f.getImgURL());
			}
		else
			Log.i(tag, "no friends");
	}

	class ItemsAdapter extends ArrayAdapter<Button> {

		private Button[] items;

		public ItemsAdapter(Context context, int textViewResourceId, Button[] items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Button b = items[position];

			return b;
		}

	}
}