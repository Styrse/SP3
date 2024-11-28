package src;

import java.time.Year;
import java.util.ArrayList;

public class StreamingPlatform {
    private String appName;
    private ArrayList<User> users;
    private ArrayList<Media> medias;
    private ArrayList<Movie> movies;
    private ArrayList<Series> series;
    private Media currentMedia;
    private User currentUser;
    private Menu menu;
    private Search search;
    private Load load;

    boolean on = true;

    public StreamingPlatform(String appName) {
        this.appName = appName;
        this.users = new ArrayList<User>();
        this.medias = new ArrayList<Media>();
        this.movies = new ArrayList<Movie>();
        this.series = new ArrayList<Series>();
        this.menu = new Menu();
        this.search = new Search();
        this.load = new Load(users, movies, medias, series);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addMedia(Media media) {
        medias.add(media);
    }

    public void removeMedia(Media media) {
        medias.remove(media);
    }

    public void userRegister() {

        String username = username();
        String password = password();
        int birthdayYear = birthyear();
        String gender = gender();

        User user = new User(username, password, birthdayYear, gender);
        users.add(user);
        currentUser = user;
        TextUI.displayMSG("You have now been registered");

    }

    public String gender() {
        String gender = TextUI.promptText("Please enter gender, You have 5 choices:" +
                "\nFemale (F), Male(M), Non-binary(N), Transgender(T), Other(O), Prefer not to say(D)" +
                "\nGender: ").toUpperCase();
        switch (gender) {
            case "F":
                gender = "Female";
                break;
            case "M":
                gender = "Male";
                break;
            case "N":
                gender = "Non-binary";
                break;
            case "T":
                gender = "Transgender";
                break;
            case "O":
                gender = "Other";
                break;
            case "D":
                gender = null;
                break;
            default:
                gender = null;
        }
        return gender;
    }

    public String password() {
        String password = TextUI.promptText("Please enter password: ");
        if (password.length() < 6 || !password.matches(".*[0-9].*") || !checkUpperCase(password)){
            TextUI.displayMSG("Password must be at least 6 character, contain a number and one capital letter. Please try again");
            password = password();
        }
        return password;
    }

    public boolean checkUpperCase(String password){
        char character;
        for (int i = 0; i < password.length(); i++){
            character = password.charAt(i);
            if (Character.isUpperCase(character)) {
                return true;
            }
        }
        return false;
    }

    public String username() {
        String username = TextUI.promptText("Please enter username: ");
        if (checkForDuplicateUser(username)) {
            TextUI.displayMSG("The username is already taken, please chose another one.");
            username = username();
        }
        return username;
    }

    public boolean checkForDuplicateUser(String username) {
        boolean isDuplicate = false;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                isDuplicate = true;
            }
        }
        return isDuplicate;
    }

    public int birthyear() {
        int birthyear = TextUI.promptNumeric("Please enter birth year(YYYY): ");
        if (birthyear < Year.now().getValue() - 125) {
            TextUI.displayMSG("Birth year must be realistic.");
            birthyear();
        } else if (birthyear > Year.now().getValue()) {
            TextUI.displayMSG("Birth year cannot be in the future.");
            birthyear();
        }
        return birthyear;
    }

    public void userLogin() {
        TextUI.displayMSG("You have chosen to login");
        String username = TextUI.promptText("Please enter your username: ");
        String password = TextUI.promptText("Please enter your password: ");

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) { //the code is cheking if the username and passeword is in the file, if both is correct.
                TextUI.displayMSG(user.getUsername() + " has logged in");
                currentUser = user;
                return;
            }
        }
        TextUI.displayMSG("Login has failed. Username or password is incorrect");
        String flag = TextUI.promptText("Do you want to login(L) or register(R)? ");
        if (flag.equalsIgnoreCase("L")) {
            userLogin();
        } else if (flag.equalsIgnoreCase("R")) {
            userRegister();
        } else {
            TextUI.displayMSG("Invalid option");
            userLoginOrRegister();
        }
    }

    public void userLoginOrRegister() {
        TextUI.displayMSG("Welcome to our WBBTServices \n" +
                "Login = (L) \n" +
                "Register = (R)");

        String choice = TextUI.promptText("Do you want to login to an existing account or register a new account? ");
        if (choice.equalsIgnoreCase("L")) {
            userLogin();
        } else if (choice.equalsIgnoreCase("R")) {
            userRegister();
        } else {
            TextUI.displayMSG("Invalid option. Redirecting to start menu");
            userLoginOrRegister();
        }
    }

    public void setup() {
        load.loadUsers();
        load.loadMovies();
        load.loadSeries();
        userLoginOrRegister();
        runLoop();
    }

    public void chooseMovie(){
        int choice = TextUI.promptNumeric("Please write the number of the movie you want to choose: ");
        // Check if the input is valid:
        if (choice < 1 || choice > movies.size()) {
            TextUI.displayMSG("Invalid choice. Please select a number from the list.");
            chooseMovie();
            return;
        }
        currentMedia = movies.get(choice - 1); //Get the chosen movie and convert user input to 0-based index:
        TextUI.displayMSG("You selected: " + currentMedia.getMediaName() + "\nIMDB Score: " + currentMedia.getIMDBScore());
        mediaActionMenu();
    }

    public void mediaAction(Media media)   {
        TextUI.displayMSG("Title: " + media.getMediaName() +
                "\nIMDBScore:" + media.getIMDBScore());
        mediaActionMenu();
    }

    public void mediaActionMenu(){
        String tmpChoice;
        if (currentUser.getSaved().contains(currentMedia)){
            tmpChoice = TextUI.promptText("You have the following options: Play(P), Remove from savedList(R), Main menu(M), Remove from specialPlayList(S)");
            if (tmpChoice.equalsIgnoreCase("P")){
                playMedia();
            } else if (tmpChoice.equalsIgnoreCase("R")) {
                currentUser.removeFromSaved(currentMedia);
                TextUI.displayMSG("You have now removed: " + currentMedia.getMediaName() +" from your savedList");
            } else if (tmpChoice.equalsIgnoreCase("M")) {
                mainMenu();
            } else if (tmpChoice.equalsIgnoreCase("S")) {
                currentUser.removeFromSpecialPlayLists(currentMedia);
                TextUI.displayMSG("You have now removed: " + currentMedia.getMediaName() +" from your specialPlayList");
            } else {
                TextUI.displayMSG("Invalid choice. Please try again");
                mediaActionMenu();
            }
        } else {
            tmpChoice = TextUI.promptText("You have the following options: Play(P), Add to savedList(A), Main menu(M), Add to specialPlayList(S)\n" +
                    "Please enter your choice: ");
            if (tmpChoice.equalsIgnoreCase("P")){
                playMedia();
            } else if (tmpChoice.equalsIgnoreCase("A")) {
                currentUser.addToSaved(currentMedia);
                TextUI.displayMSG("You have now added: " + currentMedia.getMediaName() +" from your savedList");
            } else if (tmpChoice.equalsIgnoreCase("M")) {
                mainMenu();
            } else if (tmpChoice.equalsIgnoreCase("S")) {
                currentUser.addToSpecialPlayLists(currentMedia);
                TextUI.displayMSG("You have now added: " + currentMedia.getMediaName() +" to your specialPlayList");
            } else {
                TextUI.displayMSG("Invalid choice. Please try again");
                mediaActionMenu();
            }
        }
    }

    public void playMedia()   {
        TextUI.displayMSG("Now watching: " + currentMedia.getMediaName());
        currentUser.addToSeen(currentMedia);
    }

    public void movies(){
        for (int i = 0; i < movies.size(); i++){
            TextUI.displayMSG(i+1 + " " + movies.get(i).getMediaName());
        }
    }

    public void runLoop(){
        while (on){
            mainMenu();
        }
    }

    public void mainMenu(){
        String menuChoice = menu.mainMenu();
        if (menuChoice.equalsIgnoreCase("M")){
            TextUI.displayMSG("Movies: ");
            movies();
            chooseMovie();
        } else if (menuChoice.equalsIgnoreCase("S")) {
            TextUI.displayMSG("Series - to be done");
        } else if (menuChoice.equalsIgnoreCase("LI")) {
            TextUI.displayMSG("Lists");
            menu.listMenu(currentUser);
        } else if (menuChoice.equalsIgnoreCase("F")) {
            currentMedia = search.searchByTitle(medias);
            mediaAction(currentMedia);
        } else if (menuChoice.equalsIgnoreCase("SET")) {
            userSettingsMenu();
        } else if (menuChoice.equalsIgnoreCase("LO")) {
            TextUI.displayMSG("Thank you for watching today.");
            end();
            on = false;
        }
    }

    public void userSettingsMenu(){
        TextUI.displayMSG("=====Settings=====");
        String tmpChoice = TextUI.promptText("Change username(U), Change password(C), Delete account(D), Main menu(M)\n" +
                "Enter choice: ");
        if (tmpChoice.equalsIgnoreCase("U")){
            currentUser.setUsername(username());
        } else if (tmpChoice.equalsIgnoreCase("C")) {
            currentUser.setPassword(password());
        } else if (tmpChoice.equalsIgnoreCase("D")) {
            users.remove(currentUser);
            end();
            on = false;
        } else if ((tmpChoice.equalsIgnoreCase("M"))) {
            menu.mainMenu();
        }
    }

    public void end() {
        usersToText();
        saveUserLists();
    }

    public void saveUserLists(){
        currentUser.mediaToString(currentUser.getSaved(), "Saved");
        currentUser.mediaToString(currentUser.getSeen(), "Seen");
        currentUser.mediaToString(currentUser.getSpecialPlayLists(), "Special");
    }

    public void usersToText(){
        ArrayList<String> userssAsText = new ArrayList<>();
        for (User u : users) {
            userssAsText.add(u.toString());
        }
        FileIO.saveData(userssAsText, "data/userdata.csv");
    }
}
