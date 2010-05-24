/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.international.timezone;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.international.Changed;
import org.joda.time.DateTimeZone;

/**
 * TimeZone for a User Session. Defaults to the TimeZone within DefaultTimeZone
 * and is altered when it receives the @Changed event.
 * 
 * @author Ken Finnigan
 */

@SessionScoped
public class UserTimeZoneProducer implements Serializable
{
   private static final long serialVersionUID = -9008203923830420841L;

   @Produces
   @UserTimeZone
   @Named
   private DateTimeZone userTimeZone;

   @Inject
   public void init(DateTimeZone defaultTimeZone)
   {
      this.userTimeZone = defaultTimeZone;
   }

   public void changeTimeZone(@Observes @Changed DateTimeZone tz)
   {
      this.userTimeZone = tz;
   }
}