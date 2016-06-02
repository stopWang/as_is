package app.com.wj.model;

/**
 * Created by Administrator on 2016/4/28.
 * 律诗的数据模型
 */
public class Lvshi_item {
    private String title;
    private String text;
    private String author;
    private int zan;
    private int comment_numb;

    public Lvshi_item(String title, String text, String author, int zan, int comment_numb) {
        this.title = title;
        this.text = text;
        this.author = author;
        this.zan = zan;
        this.comment_numb = comment_numb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getComment_numb() {
        return comment_numb;
    }

    public void setComment_numb(int comment_numb) {
        this.comment_numb = comment_numb;
    }

    public Lvshi_item() {

    }
}
