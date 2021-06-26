package mk.ukim.finki.wbs.model.enumeration;

public enum PropertyType {
    title("title"),
    description("description"),
    explanation("explanation"),
    writer("writer"),
    likedBy("likedBy"),
    dislikedBy("dislikedBy"),
    tag("tag"),
    hasReply("term_has_reply"),
    replyOf("term_reply_of"),
    date("date"),
    username("username"),
    email("email"),
    code("code"),
    role("role"),
    prefix("prefix"),
    uri("identifier");

    private String type;

    PropertyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
