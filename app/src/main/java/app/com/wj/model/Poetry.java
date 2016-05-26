package app.com.wj.model;
/**
 * 诗词文章的模型
 * @author Administrator
 *
 */
public class Poetry {
		private String poetry_id;
		private String poetry_title;
		private String poetry_author;
		private String poetry_context;
		private String poetry_type;
		private String poetry_praise_numb;
		private String poetry_praise_comment;
		public String getPoetry_id() {
			return poetry_id;
		}
		public void setPoetry_id(String poetry_id) {
			this.poetry_id = poetry_id;
		}
		public String getPoetry_title() {
			return poetry_title;
		}
		public void setPoetry_title(String poetry_title) {
			this.poetry_title = poetry_title;
		}
		public String getPoetry_author() {
			return poetry_author;
		}
		public void setPoetry_author(String poetry_author) {
			this.poetry_author = poetry_author;
		}
		public String getPoetry_context() {
			return poetry_context;
		}
		public void setPoetry_context(String poetry_context) {
			this.poetry_context = poetry_context;
		}
		public String getPoetry_type() {
			return poetry_type;
		}
		public void setPoetry_type(String poetry_type) {
			this.poetry_type = poetry_type;
		}
		public String getPoetry_praise_numb() {
			return poetry_praise_numb;
		}
		public void setPoetry_praise_numb(String poetry_praise_numb) {
			this.poetry_praise_numb = poetry_praise_numb;
		}
		public String getPoetry_praise_comment() {
			return poetry_praise_comment;
		}
		public void setPoetry_praise_comment(String poetry_praise_comment) {
			this.poetry_praise_comment = poetry_praise_comment;
		}
		public Poetry(String poetry_id, String poetry_title,
				String poetry_author, String poetry_context,
				String poetry_type, String poetry_praise_numb,
				String poetry_praise_comment) {
			super();
			this.poetry_id = poetry_id;
			this.poetry_title = poetry_title;
			this.poetry_author = poetry_author;
			this.poetry_context = poetry_context;
			this.poetry_type = poetry_type;
			this.poetry_praise_numb = poetry_praise_numb;
			this.poetry_praise_comment = poetry_praise_comment;
		}
		public Poetry() {
			super();
		}
}
