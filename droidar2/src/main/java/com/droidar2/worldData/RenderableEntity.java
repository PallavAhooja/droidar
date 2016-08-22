package com.droidar2.worldData;

import com.droidar2.gl.LightSource;
import com.droidar2.gl.Renderable;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gl.scenegraph.RenderList;
import com.droidar2.gl.scenegraph.Shape;

/**
 * This is the basic interface for any object which hat to do with Rendering and
 * which also needs to be updated from time to time. <br>
 * <br>
 * 
 * The existing important subclasses are: <br>
 * 
 * - {@link RenderList}: It is a group of {@link RenderableEntity}s<br>
 * 
 * - {@link MeshComponent}: A basic {@link Shape} e.g. to draw OpenGL objects or
 * {@link LightSource} to add lighning effects to a scene <br>
 * 
 * @author Spobo
 * 
 */
public interface RenderableEntity extends Entity, Renderable {

}
