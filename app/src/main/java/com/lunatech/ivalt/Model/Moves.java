package com.lunatech.ivalt.Model;

public class Moves {

    private String moveName;
    private String moveType;
    private String moveCategory;
    private String movePower;
    private String moveAccuracy;

    public Moves() {
    }

    public Moves(String moveName, String moveType, String moveCategory, String movePower, String moveAccuracy) {
        this.moveName = moveName;
        this.moveType = moveType;
        this.moveCategory = moveCategory;
        this.movePower = movePower;
        this.moveAccuracy = moveAccuracy;
    }

    @Override
    public String toString() {
        return "Moves{" +
                "moveName='" + moveName + '\'' +
                ", moveType='" + moveType + '\'' +
                ", moveCategory='" + moveCategory + '\'' +
                ", movePower='" + movePower + '\'' +
                ", moveAccuracy='" + moveAccuracy + '\'' +
                '}';
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getMoveCategory() {
        return moveCategory;
    }

    public void setMoveCategory(String moveCategory) {
        this.moveCategory = moveCategory;
    }

    public String getMovePower() {
        return movePower;
    }

    public void setMovePower(String movePower) {
        this.movePower = movePower;
    }

    public String getMoveAccuracy() {
        return moveAccuracy;
    }

    public void setMoveAccuracy(String moveAccuracy) {
        this.moveAccuracy = moveAccuracy;
    }
}
