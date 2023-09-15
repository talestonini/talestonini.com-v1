package com.talestonini

import scala.scalajs.js
import js.annotation.JSImport
import js.JSConverters.JSRichMap

object Firebase {

  @js.native @JSImport("firebase/app", "initializeApp")
  def initializeApp(firebaseConfig: js.Dictionary[String]): js.Object = js.native
  // Add SDKs for Firebase products that you want to use
  // https://firebase.google.com/docs/web/setup#available-libraries

  // Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
  private val firebaseConfig = js.Dictionary(
    "apiKey"            -> "@API_KEY@",
    "authDomain"        -> "@AUTH_DOMAIN@",
    "databaseURL"       -> "@DATABASE_URL@",
    "projectId"         -> "@PROJECT_ID@",
    "storageBucket"     -> "@STORAGE_BUCKET@",
    "messagingSenderId" -> "@MESSAGING_SENDER_ID@",
    "appId"             -> "@APP_ID@",
    "measurementId"     -> "@MEASUREMENT_ID@"
  )

  // Initialize Firebase
  private val app = initializeApp(firebaseConfig)

  // --- analytics -----------------------------------------------------------------------------------------------------

  @js.native @JSImport("firebase/analytics", "getAnalytics")
  def getAnalytics(app: js.Object): js.Object = js.native

  @js.native @JSImport("firebase/analytics", "logEvent")
  def logEvent(analytics: js.Object, eventName: String, event: js.Dictionary[Any]): Unit = js.native

  // Initialize Analytics and get a reference to the service
  private val analytics = getAnalytics(app)

  private def logEvent(eventName: String, event: Map[String, Any]): Unit =
    logEvent(analytics, eventName, event.toJSDictionary)

  def gaViewing(page: String): Unit =
    logEvent("viewing", Map("page" -> page))

  def gaCommentedOn(post: String): Unit =
    logEvent("commented_on", Map("post" -> post))

  def gaClickedSharing(post: String, how: String): Unit =
    logEvent("clicked_sharing", Map("post" -> post, "how" -> how))

  def gaClickedFooter(target: String): Unit =
    logEvent("clicked_footer", Map("target" -> target))

}
