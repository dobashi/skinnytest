package integrationtest

import jp.lavans.skinnytest.controller.Controllers
import org.scalatest._
import skinny._
import skinny.test._
import org.joda.time._
import jp.lavans.skinnytest.model._

class UsersController_IntegrationTestSpec extends SkinnyFlatSpec with SkinnyTestSupport with BeforeAndAfterAll with DBSettings {
  addFilter(Controllers.users, "/*")

  override def afterAll() {
    super.afterAll()
    User.deleteAll()
  }

  def newUser = FactoryGirl(User).create()

  it should "show users" in {
    get("/users") {
      logBodyUnless(200)
      status should equal(200)
    }
    get("/users/") {
      logBodyUnless(200)
      status should equal(200)
    }
    get("/users.json") {
      logBodyUnless(200)
      status should equal(200)
    }
    get("/users.xml") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "show a user in detail" in {
    get(s"/users/${newUser.id}") {
      logBodyUnless(200)
      status should equal(200)
    }
    get(s"/users/${newUser.id}.xml") {
      logBodyUnless(200)
      status should equal(200)
    }
    get(s"/users/${newUser.id}.json") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "show new entry form" in {
    get(s"/users/new") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "create a user" in {
    post(s"/users",
      "name" -> "dummy",
      "email" -> "dummy",
      "created_at" -> skinny.util.DateTimeUtil.toString(new DateTime()),
      "updated_at" -> skinny.util.DateTimeUtil.toString(new DateTime())) {
      logBodyUnless(403)
      status should equal(403)
    }

    withSession("csrf-token" -> "valid_token") {
      post(s"/users",
        "name" -> "dummy",
        "email" -> "dummy",
        "created_at" -> skinny.util.DateTimeUtil.toString(new DateTime()),
        "updated_at" -> skinny.util.DateTimeUtil.toString(new DateTime()),
        "csrf-token" -> "valid_token") {
        logBodyUnless(302)
        status should equal(302)
        val id = header("Location").split("/").last.toLong
        User.findById(id).isDefined should equal(true)
      }
    }
  }

  it should "show the edit form" in {
    get(s"/users/${newUser.id}/edit") {
      logBodyUnless(200)
      status should equal(200)
    }
  }

  it should "update a user" in {
    put(s"/users/${newUser.id}",
      "name" -> "dummy",
      "email" -> "dummy",
      "created_at" -> skinny.util.DateTimeUtil.toString(new DateTime()),
      "updated_at" -> skinny.util.DateTimeUtil.toString(new DateTime())) {
      logBodyUnless(403)
      status should equal(403)
    }

    withSession("csrf-token" -> "valid_token") {
      put(s"/users/${newUser.id}",
        "name" -> "dummy",
        "email" -> "dummy",
        "created_at" -> skinny.util.DateTimeUtil.toString(new DateTime()),
        "updated_at" -> skinny.util.DateTimeUtil.toString(new DateTime()),
        "csrf-token" -> "valid_token") {
        logBodyUnless(302)
        status should equal(302)
      }
    }
  }

  it should "delete a user" in {
    delete(s"/users/${newUser.id}") {
      logBodyUnless(403)
      status should equal(403)
    }
    withSession("csrf-token" -> "valid_token") {
      delete(s"/users/${newUser.id}?csrf-token=valid_token") {
        logBodyUnless(200)
        status should equal(200)
      }
    }
  }

}
