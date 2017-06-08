/*

    PraFramework - A simple TCP-Networking framework for Java
    Copyright (C) 2017  Zortax

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package de.zortax.pra.server.command;//  Created by Leonard on 03.03.2017.

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PraCommand {

    /**
     * @return The name of this command
     */
    String name();

    /**
     * @return short usage instructions
     */
    String usage();

    /**
     * @return a short description of this command
     */
    String description() default "No description";

    /**
     * @return the permission that is needed to execute this command
     */
    String permission() default "none";

    /**
     * @return the minimum argument count for this command (-1: no limit)
     */
    int minArgs() default -1;

    /**
     * @return the maximum argument count for this command (-1: no limit)
     */
    int maxArgs() default -1;

    /**
     * @return if this argument should be executed async to the input thread
     */
    boolean async() default false;

}
