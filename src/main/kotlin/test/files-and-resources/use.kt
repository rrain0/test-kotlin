package test.`files-and-resources`



interface XMLWriter {
  fun document(encoding: String, version: String, content: XMLWriter.() -> Unit)
  fun element(name: String, content: XMLWriter.() -> Unit)
  fun attribute(name: String, value: String)
  fun text(value: String)
  
  fun flushAndClose()
}

fun writeBooksTo(writer: XMLWriter) {
  
  // interface AutoClosable
  // create AutoClosable instance and define how to auto-close
  val autoCloseable = AutoCloseable { writer.flushAndClose() }
  
  // use
  autoCloseable.use {
    writer.document(encoding = "UTF-8", version = "1.0") {
      element("bookstore") {
        element("book") {
          attribute("category", "fiction")
          element("title") { text("Harry Potter and the Prisoner of Azkaban") }
          element("author") { text("J. K. Rowling") }
          element("year") { text("1999") }
          element("price") { text("29.99") }
        }
        element("book") {
          attribute("category", "programming")
          element("title") { text("Kotlin in Action") }
          element("author") { text("Dmitry Jemerov") }
          element("author") { text("Svetlana Isakova") }
          element("year") { text("2017") }
          element("price") { text("25.19") }
        }
      }
    }
  }
}