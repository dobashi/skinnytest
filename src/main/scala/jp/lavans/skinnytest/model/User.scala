package jp.lavans.skinnytest.model

import skinny.orm._, feature._
import scalikejdbc._
import org.joda.time._

case class User(
  id: Long,
  name: String,
  email: String,
  createdAt: DateTime,
  updatedAt: DateTime,
)

object User extends SkinnyCRUDMapper[User] with TimestampsFeature[User] {
  override lazy val tableName = "users"
  override lazy val defaultAlias = createAlias("u")

  /*
   * If you're familiar with ScalikeJDBC/Skinny ORM, using #autoConstruct makes your mapper simpler.
   * (e.g.)
   * override def extract(rs: WrappedResultSet, rn: ResultName[User]) = autoConstruct(rs, rn)
   *
   * Be aware of excluding associations like this:
   * (e.g.)
   * case class Member(id: Long, companyId: Long, company: Option[Company] = None)
   * object Member extends SkinnyCRUDMapper[Member] {
   *   override def extract(rs: WrappedResultSet, rn: ResultName[Member]) =
   *     autoConstruct(rs, rn, "company") // "company" will be skipped
   * }
   */
  override def extract(rs: WrappedResultSet, rn: ResultName[User]): User = new User(
    id = rs.get(rn.id),
    name = rs.get(rn.name),
    email = rs.get(rn.email),
    createdAt = rs.get(rn.createdAt),
    updatedAt = rs.get(rn.updatedAt)
  )
}
