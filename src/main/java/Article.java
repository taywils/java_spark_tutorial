import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
    private String title;
    private String content;
    private Date createdAt;
    private String summary;
    private Integer id;
    private Boolean deleted;

    public Article(String title, String summary, String content, Integer size) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.createdAt = new Date();
        this.id = size;
        this.deleted = false;
    }

    public Article(String title, String summary, String content, Integer id, Date createdAt, Boolean deleted) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.createdAt = createdAt;
        this.id = id;
        this.deleted = deleted;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getId() {
        return id;
    }

    public void delete() {
        this.deleted = true;
    }

    public Boolean readable() {
        return !this.deleted;
    }

    public String getCreatedAt() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(this.createdAt);
    }

    public String getEditLink() {
        return "<a href='/article/update/" + this.id + "'>Edit</a>";
    }

    public String getDeleteLink() {
        return "<a href='/article/delete/" + this.id + "'>Delete</a>";
    }

    public String getSummaryLink() {
        return "<a href='/article/read/" + this.id + "'>" + this.summary + "</a>";
    }
}
