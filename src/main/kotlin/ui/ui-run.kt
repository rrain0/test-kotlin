package ui



fun main(){
  
  val root = div {
    width = 1920.px
    height = 1080.px
    
    var firstHalf: Node
    div {
      firstHalf = this
      width = 50.percent
      height = full
      div {
        +"text node 1"
        width = 50.percent
        height = full
        div {
          setWidth(30.percent, firstHalf.getWidth())
          width = 30.percent
          height = full
        }
        +"text node 2"
      }
    }
    div {
      width = 50.percent
      height = full
    }
  }
  
  
}


