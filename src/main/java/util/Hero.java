package models;

public class Hero {
    private String mName;
    private int mAge;
    private String mPower;
    private String mWeakness;
    private int mId;

    private static List<Hero> heroes =new ArrayList<>();

    public Hero(String name,int age,String power,String weakness ) {
        mName = name;
        mAge = age;
        mPower = power;
        mWeakness = weakness;
        heroes.add(this);
        mId = heroes.size();
    }

    public String getName() {
        return mName;
    }
    public int getAge() {
        return mAge;
    }
    public String getPower() {
        return mPower;
    }

    public String getWeakness() {
        return mWeakness;
    }
    public int getId() {
        return mId;
    }

    public static List<Hero> all() {
        return heroes;
    }

    public static void clear() {
        heroes.clear();
    }

    public static Hero find(int id) {
        return heroes.get(id - 1);
    }
}
