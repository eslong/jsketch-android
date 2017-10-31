README file for eslong (20503353) - CS 349, Assignment 3

Files (src)
	- MainActivity.java: Main activity for the app that handles all toggle buttons and radio buttons, and also handles the saving/restoring of data on orientation change
	- CanvasView.java: Handles all drawing interactions (touch/drag/release) and draws all shapes
	- LinePreview.java: Draws a sample line to display current line thickness
	- Shapes.java: abstract model for the various shapes that can be drawn by the user
	- Line.java: model for a Line shape drawn by the user, storing all necessary information to draw a line on the canvas
	- Rectangle.java: model for a Rectangle shape drawn by the user, storing all necessary information to draw a Rectangle on the canvas
	- Oval.java: model for an Oval shape drawn by the user, storing all necessary information to draw a Rectangle on the canvas 
	- Model.java: model that handles all interactions with the data to draw the canvas; stores an array of Shapes to know the current shapes on the canvas, which the CanvasPanel view itterates through in order to paint the canvas

Tools
	- Selection tool: allows the user to select a shape on the canvas; selected shapes will have a dashed border instead of a whole border; clicking and dragging a selected shape will allow the user to move it around the canvas; while selected, line thickness and line color can be changed by changing them in the corresponding panels on the toolbar.
	- Erase tool: clicking a shape on the canvas will remove that shape from the canvas
	- Line tool: clicking and dragging will create a line with the currently selected color as the line color and the current line thickness as the thickness of the drawn line; preview line is displayed as the mouse is dragged
	- Rectangle tool: clicking and dragging will draw a rectangle, and this rectangle can be drawn between the original click location and wherever the mouse is currently located on the canvas
	- Oval tool: clicking and dragging will draw a circle with a diameter that is equal to the x-coordinate diference between the initial click point and the current mouse pointer location within the canvas
	- Fill tool: clicking a shape on the canvas (that is not a line) will fill the shape with whatever the currently selected color is.

Missing functionality
	- Line select/drag: I ran into issues trying to figure out how to accomplish this with android libraries (I used the java.awt.Line2D.intersect method in A2) and as such functionality was excluded
	- Greater indication of which color is selected: I was unable to get a solution to programmatically setting a dashed border for the color palette toggle buttons (since the background overrides the default toggle) and was unable to finish it in time. 
	- Icons for tool buttons: Again, couldn't figure it out in time so stuck with simple text

Enhancements
	- No enahncements were added