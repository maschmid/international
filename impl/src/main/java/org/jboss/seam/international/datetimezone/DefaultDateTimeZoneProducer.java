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
package org.jboss.seam.international.datetimezone;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.jboss.seam.international.datetimezone.ForwardingDateTimeZone;
import org.jboss.seam.international.timezone.DefaultTimeZone;
import org.joda.time.DateTimeZone;

/**
 * Default DateTimeZone of the application. If configuration of the default DateTimeZone is found that will be used, otherwise
 * the JVM default TimeZone.
 * 
 * @author Ken Finnigan
 */

@ApplicationScoped
public class DefaultDateTimeZoneProducer implements Serializable
{
   private static final long serialVersionUID = 6181892144731122500L;

   @Inject
   @DefaultTimeZone
   private Instance<String> defaultTimeZoneId;

   private final Logger log = Logger.getLogger(DefaultDateTimeZoneProducer.class);

   @Produces
   @Named
   private DateTimeZone defaultDateTimeZone = null;

   @PostConstruct
   public void init()
   {
      if (!defaultTimeZoneId.isUnsatisfied())
      {
         try
         {
            String id = defaultTimeZoneId.get();
            DateTimeZone dtz = DateTimeZone.forID(id);
            defaultDateTimeZone = constructTimeZone(dtz);
         }
         catch (IllegalArgumentException e)
         {
            log.warn("DefaultDateTimeZoneProducer: Default TimeZone Id of " + defaultTimeZoneId + " was not found");
         }
      }
      if (null == defaultDateTimeZone)
      {
         DateTimeZone dtz = DateTimeZone.getDefault();
         defaultDateTimeZone = constructTimeZone(dtz);
      }
   }

   private ForwardingDateTimeZone constructTimeZone(final DateTimeZone dtz)
   {
      return new ForwardingDateTimeZone(dtz.getID())
      {
         @Override
         protected DateTimeZone delegate()
         {
            return dtz;
         }
      };
   }
}
