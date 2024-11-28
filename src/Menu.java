package src;

import java.util.ArrayList;
import java.util.Arrays;

public class Menu {

    public String mainMenu(){
        ArrayList<String> menu = new ArrayList<>(Arrays.asList("Movies(M)", "Series(S)", "Lists(LI)", "Search(F)", "Settings(SET)", "Logout(LO)"));
        TextUI.displayMSG("=====MENU=====");
        TextUI.displayMSG(String.valueOf(menu));
        return TextUI.promptText("Please enter what you want to do: ");
    }

    public void listMenu(User currentUser){

        // Create a menu where you can choose a list you want to see
        ArrayList<String> listMenu = new ArrayList<>(Arrays.asList("SavedList(SA)", "SeenList(SE)", "SpecialPlayList(SP)"));
        TextUI.displayMSG("=====LISTMENU=====");
        TextUI.displayMSG(String.valueOf(listMenu));

        // useing the promptText to user for a choise
        String choice = TextUI.promptText("Please enter what list, you want to see: ");

        //if user choose SeenList
        if(choice.equalsIgnoreCase("SE")){
            TextUI.displayMSG("Here is your seenList: ");
            ArrayList<Media> userSeenList = currentUser.getSeen();
            if(userSeenList.isEmpty()){
                TextUI.displayMSG("Your seenList is empty");
                mainMenu();
            }else{
                for(Media media : userSeenList) {
                    TextUI.displayMSG(media.getMediaName());
                }
            }
        }else if(choice.equalsIgnoreCase("SA")){ // if emty, tell and go to media
            TextUI.displayMSG("Here is your savedList: ");
            ArrayList<Media> userSavedList = currentUser.getSaved();
            if(userSavedList.isEmpty()){
                TextUI.displayMSG("Your savedList is empty");
                mainMenu();
            }else{
                for (Media media : userSavedList){
                    TextUI.displayMSG((media.getMediaName()));
                }
            }
        }else if(choice.equalsIgnoreCase("SP")){
            TextUI.displayMSG("Here is your specialPlayLists you made: ");
            ArrayList<Media> userSpecialPlayListes = currentUser.getSpecialPlayLists();
            if(userSpecialPlayListes.isEmpty()){
                TextUI.displayMSG("Your specialPlayList is empty");
                mainMenu();
            }else{
                int index = 1;
                for(Media playList: userSpecialPlayListes){
                    TextUI.displayMSG("PlayList "+ index + ": ");
                    for(Media media: userSpecialPlayListes){
                        TextUI.displayMSG(media.getMediaName());
                    }
                    index++;
                }
            }
        }else{
            TextUI.displayMSG("Invalid choice. Please choose a valid list( SA, SE, SP)");
        }
    }
}