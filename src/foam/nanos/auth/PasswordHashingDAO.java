/**
 * @license
 * Copyright 2017 The FOAM Authors. All Rights Reserved.
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package foam.nanos.auth;

import foam.core.FObject;
import foam.core.X;
import foam.dao.DAO;
import foam.dao.ProxyDAO;
import foam.util.Password;
import foam.util.SafetyUtil;

import java.util.Calendar;
import java.util.TimeZone;

public class PasswordHashingDAO
    extends ProxyDAO
{
  public PasswordHashingDAO(X x, DAO delegate) {
    setX(x);
    setDelegate(delegate);
  }

  @Override
  public FObject put_(X x, FObject obj) {
    User user = (User) obj;
    // don't hash the password if desired password isn't set
    if ( SafetyUtil.isEmpty(user.getDesiredPassword()) ) {
      return super.put_(x, obj);
    }

    // hash password if result does not exist or does not have password set
    User stored = (User) getDelegate().find(user.getId());
    if ( stored == null || SafetyUtil.isEmpty(stored.getPassword()) ) {
      user.setPassword(Password.hash(user.getDesiredPassword()));
      user.setPreviousPassword(null);
      user.setPasswordLastModified(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
    } else {
      // set the incoming user's password to the stored password
      user.setPassword(stored.getPassword());
      user.setPreviousPassword(stored.getPreviousPassword());
      user.setPasswordLastModified(stored.getPasswordLastModified());
    }

    return super.put_(x, user);
  }
}
