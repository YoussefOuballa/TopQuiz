package ma.snrt.topquiz.model;

public class User {

    private String mFirstname;
    private int mScore;

    public String getFirstname(){
        return mFirstname;
    }

    public void setFirstName(String firstname){
        mFirstname = firstname;
    }

    public int getScore(){
        return mScore;
    }

    public void setScore(int score){
        mScore = score;
    }

    @Override
    public String toString() {
        return "User{" + "mFirstname='" + mFirstname + '\'' + '}';
    }
}
