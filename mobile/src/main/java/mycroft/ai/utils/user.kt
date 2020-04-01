package mycroft.ai.utils

object User{

    init {
        println("Singleton class invoked.")
    }
    var UserName = "demo.ekylibre.io"
    var Token = "Potentielleorigine"
    fun printVarName(){
        println(UserName)
    }

}

fun main(name: String, Token: String) {
    User.printVarName()
    User.UserName = name
    User.Token = Token
}