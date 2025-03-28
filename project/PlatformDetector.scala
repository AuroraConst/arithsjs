object PlatformDetector {
  def main(args: Array[String]): Unit = {
    val osName = System.getProperty("os.name").toLowerCase
    println(s"Detected platform: $osName")

    val platform =
      if (osName.contains("win")) "Windows"
      else if (osName.contains("mac")) "Darwin/macOS"
      else if (osName.contains("nix") || osName.contains("nux")) "Linux/Unix"
      else "Unknown"

    println(s"Platform interpreted as: $platform")

    val separator = platform match {
      case "Windows" => "\\"
      case _ => "/"
    }

    println(s"Using file path separator: '$separator'")


    if (osName.isEmpty)
      throw new RuntimeException("OS name should not be empty!")
  }
}
