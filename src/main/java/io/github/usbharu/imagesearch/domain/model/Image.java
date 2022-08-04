package io.github.usbharu.imagesearch.domain.model;

public class Image {

  private final String name;
  private final String url;

  private final int id;


  public Image(String name, String url) {
    this.name = name;
    this.url = url;
    this.id = -1;
  }

  public Image(int id,String name,String url){
    this.name = name;
    this.url = url;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public int getId(){
    return id;
  }
}
