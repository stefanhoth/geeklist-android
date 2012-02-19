package st.geekli.android;

public class TrendingUserItem {
  private String       thumbnail;
  private CharSequence name;
  private CharSequence user;
  private CharSequence content;

  /**
   * @return the thumbnail
   */
  public String getThumbnail() {
    return thumbnail;
  }

  /**
   * @param thumbnail
   *          the thumbnail to set
   */
  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  /**
   * @return the name
   */
  public CharSequence getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(CharSequence name) {
    this.name = name;
  }

  /**
   * @return the user
   */
  public CharSequence getUser() {
    return user;
  }

  /**
   * @param user
   *          the user to set
   */
  public void setUser(CharSequence user) {
    this.user = user;
  }

  /**
   * @return the content
   */
  public CharSequence getContent() {
    return content;
  }

  /**
   * @param content
   *          the content to set
   */
  public void setContent(CharSequence content) {
    this.content = content;
  }
}
