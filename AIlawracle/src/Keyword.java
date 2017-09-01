/*******************************************************
 * @author Zuoshan
 * @date 01/09/2017
 * A swing tool to transform docx to html,
 * search keyword in html and then italic the keyword.
 * Finally transform html back to docx.
 * 
 *******************************************************/

public class Keyword{
		
		private String text;
		private int start,end;
		
		public Keyword(String text,int start,int end) {
			this.text=text;
			this.start=start;
			this.end = end;
			filter();
		}
		
		public Keyword() {
			this.text="";
			this.end=this.start=0;
		}
		
		public String getText() {
			return this.text;
		}
		public int getStart() {
			return this.start;
		}
		public int getEnd() {
			return this.end;
		}
		public void filter() {
			if(this.text.endsWith("<")) {
				this.text=this.text.substring(0, this.text.length()-1);
				this.end-=1;
			}
		}
	}

