package mk.ukim.finki.wbs.model.enumeration;

public enum ResourceType {
    question("Question"),
    answer("Answer"),
    user("User");

    private String type;

    ResourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
