/*
 * (C) Copyright 2005-2016, by Trevor Harmon and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.ext;

import java.util.*;

/**
 * Assigns a unique integer to represent each edge. Each instance of IntegerEdgeNameProvider
 * maintains an internal map between every edge it has ever seen and the unique integer representing
 * that edge. As a result it is probably desirable to have a separate instance for each distinct
 * graph.
 * 
 * @param <E> the graph edge type
 *
 * @author Trevor Harmon
 */
public class IntegerEdgeNameProvider<E>
    implements EdgeNameProvider<E>
{
    private int nextID = 1;
    private final Map<E, Integer> idMap = new HashMap<>();

    /**
     * Clears all cached identifiers, and resets the unique identifier counter.
     */
    public void clear()
    {
        nextID = 1;
        idMap.clear();
    }

    /**
     * Returns the String representation of an edge.
     *
     * @param edge the edge to be named
     */
    @Override
    public String getEdgeName(E edge)
    {
        Integer id = idMap.get(edge);
        if (id == null) {
            id = nextID++;
            idMap.put(edge, id);
        }

        return id.toString();
    }
}

// End IntegerEdgeNameProvider.java
