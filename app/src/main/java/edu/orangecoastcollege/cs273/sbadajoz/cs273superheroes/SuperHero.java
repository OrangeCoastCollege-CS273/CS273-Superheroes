package edu.orangecoastcollege.cs273.sbadajoz.cs273superheroes;


public class SuperHero {
    private String mUserName;
    private String mName;
    private String mSuperpower;
    private String mOneThing;
    private String mFileName;

    public SuperHero(String userName, String name, String superpower, String oneThing) {
        mUserName = userName;
        mName = name;
        mSuperpower = superpower;
        mOneThing = oneThing;
        mFileName = userName + ".png";
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSuperpower() {
        return mSuperpower;
    }

    public void setSuperpower(String superpower) {
        mSuperpower = superpower;
    }

    public String getOneThing() {
        return mOneThing;
    }

    public void setOneThing(String oneThing) {
        mOneThing = oneThing;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperHero superHero = (SuperHero) o;

        if (mUserName != null ? !mUserName.equals(superHero.mUserName) : superHero.mUserName != null) return false;
        if (mName != null ? !mName.equals(superHero.mName) : superHero.mName != null) return false;
        if (mSuperpower != null ? !mSuperpower.equals(superHero.mSuperpower) : superHero.mSuperpower != null)
            return false;
        if (mOneThing != null ? !mOneThing.equals(superHero.mOneThing) : superHero.mOneThing != null) return false;
        return mFileName != null ? mFileName.equals(superHero.mFileName) : superHero.mFileName == null;

    }

    @Override
    public int hashCode() {
        int result = mUserName != null ? mUserName.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mSuperpower != null ? mSuperpower.hashCode() : 0);
        result = 31 * result + (mOneThing != null ? mOneThing.hashCode() : 0);
        result = 31 * result + (mFileName != null ? mFileName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SuperHero{" +
                "UserName='" + mUserName + '\'' +
                ", Name='" + mName + '\'' +
                ", SuperPower='" + mSuperpower + '\'' +
                ", OneThing='" + mOneThing + '\'' +
                ", FileName='" + mFileName + '\'' +
                '}';
    }
}
