package com.example.eslong.jsketch;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends Activity {

    Model model;
    CanvasView canvasView;
    LinePreview linePreview;

    ToggleButton btnSelect;
    ToggleButton btnErase;
    ToggleButton btnLine;
    ToggleButton btnRect;
    ToggleButton btnOval;
    ToggleButton btnFill;

    ToggleButton btnBlack;
    ToggleButton btnRed;
    ToggleButton btnYellow;
    ToggleButton btnBlue;
    ToggleButton btnGreen;
    ToggleButton btnOrange;

    RadioButton rdbMinLine;
    RadioButton rdbMedLine;
    RadioButton rdbMaxLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.jsketch_layout);

        model = new Model(); // create model
        canvasView = (CanvasView)findViewById(R.id.canvasView); // get canvas
        canvasView.setModel(model); // set model reference in canvas
        linePreview = (LinePreview)findViewById(R.id.linePreview); // get line preview
        linePreview.setModel(model); // set model reference in line preview

        // Get all tool buttons
        btnSelect = (ToggleButton) findViewById(R.id.btnSelect);
        btnErase = (ToggleButton) findViewById(R.id.btnErase);
        btnLine = (ToggleButton) findViewById(R.id.btnLine);
        btnRect = (ToggleButton) findViewById(R.id.btnRect);
        btnOval = (ToggleButton) findViewById(R.id.btnOval);
        btnFill = (ToggleButton) findViewById(R.id.btnFill);

        // Get all color palette buttons
        btnBlack = (ToggleButton) findViewById(R.id.btnBlack);
        btnRed = (ToggleButton) findViewById(R.id.btnRed);
        btnYellow = (ToggleButton) findViewById(R.id.btnYellow);
        btnBlue = (ToggleButton) findViewById(R.id.btnBlue);
        btnGreen = (ToggleButton) findViewById(R.id.btnGreen);
        btnOrange = (ToggleButton) findViewById(R.id.btnOrange);

        // Get all line thickness radio buttons
        rdbMinLine = (RadioButton) findViewById(R.id.rdbMinLine);
        rdbMedLine = (RadioButton) findViewById(R.id.rdbMedLine);
        rdbMaxLine = (RadioButton) findViewById(R.id.rdbMaxLine);

        // set toggles
        checkToolButtons();
        checkPaletteButtons();
        checkLineButtons();
    }

    // save and restore state (need to do this to support orientation change)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int length = model.shapes.size();
        outState.putInt("Length", length);
        for (int i = 0; i < length; i++)
        {
            if (model.shapes.get(i) instanceof Line)
            {
                outState.putString("Type-" + i, "Line");
            }
            else if (model.shapes.get(i) instanceof Oval)
            {
                outState.putString("Type-" + i, "Oval");
            }
            else if (model.shapes.get(i) instanceof Rectangle)
            {
                outState.putString("Type-" + i, "Rectangle");
            }
            outState.putFloat("OriginX-" + i, model.shapes.get(i).origin.x);
            outState.putFloat("OriginY-" + i, model.shapes.get(i).origin.y);
            outState.putFloat("EndPointX-" + i, model.shapes.get(i).endPoint.x);
            outState.putFloat("EndPointY-" + i, model.shapes.get(i).endPoint.y);
            outState.putInt("LineColor-" + i, model.shapes.get(i).lineColor);
            outState.putInt("LineThickness-" + i, model.shapes.get(i).lineThickness);
            outState.putInt("FillColor-" + i, model.shapes.get(i).fillColor);
        }

        outState.putInt("CurrLineThickness", model.lineThickness); // save selected line thickness
        outState.putInt("CurrColor", model.currColor); // save selected color
        int toolIndex = model.currTool.ordinal();
        outState.putInt("CurrTool", toolIndex);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int length = savedInstanceState.getInt("Length"); // get length of shapes array
        for (int i = 0; i < length; i++) // get all saved shapes in order
        {
            String type = savedInstanceState.getString("Type-" + i); // get type of shape
            float originX = savedInstanceState.getFloat("OriginX-" + i);
            float originY = savedInstanceState.getFloat("OriginY-" + i);
            PointF origin = new PointF(originX, originY);
            float endPointX = savedInstanceState.getFloat("EndPointX-" + i);
            float endPointY = savedInstanceState.getFloat("EndPointY-" + i);
            PointF endPoint = new PointF(endPointX, endPointY);
            int lineColor = savedInstanceState.getInt("LineColor-" + i);
            int lineThickness = savedInstanceState.getInt("LineThickness-" + i);
            int fillColor = savedInstanceState.getInt("FillColor-" + i);

            if (type == "Line")
            {
                Line shape = new Line(origin, endPoint, lineColor, lineThickness);
                model.shapes.add(shape);
            }
            else if (type == "Oval")
            {
                Oval shape = new Oval(origin, endPoint.x - origin.x, lineColor, lineThickness);
                shape.setFillColor(fillColor);
                model.shapes.add(shape);
            }
            else if (type == "Rectangle")
            {
                Rectangle shape = new Rectangle(origin, endPoint, lineColor, lineThickness);
                shape.setFillColor(fillColor);
                model.shapes.add(shape);
            }
        }

        model.setSavedCurrColor(savedInstanceState.getInt("CurrColor")); // restore selected color
        model.setSavedLineThickness(savedInstanceState.getInt("CurrLineThickness")); // restore selected line thickness
        Tool tool = Tool.values()[savedInstanceState.getInt("CurrTool")];
        model.setSavedCurrTool(tool);

        canvasView.invalidate();
    }

    // onClick event for btnSelect
    public void handleSelectButton(View v)
    {
        model.currTool = Tool.SELECT;
        model.currShape = null;
        checkToolButtons();
    }

    // onClick event for btnErase
    public void handleEraseButton(View v)
    {
        model.currTool = Tool.ERASE;
        model.currShape = null;
        checkToolButtons();
    }

    // onClick event for btnLine
    public void handleLineButton(View v)
    {
        model.currTool = Tool.LINE;
        model.currShape = null;
        checkToolButtons();
    }

    // onClick event for btnRect
    public void handleRectButton(View v)
    {
        model.currTool = Tool.RECT;
        model.currShape = null;
        checkToolButtons();
    }

    // onClick event for btnOval
    public void handleOvalButton(View v)
    {
        model.currTool = Tool.OVAL;
        model.currShape = null;
        checkToolButtons();
    }

    // onClick event for btnFill
    public void handleFillButton(View v)
    {
        model.currTool = Tool.FILL;
        model.currShape = null;
        checkToolButtons();
    }

    // onClick event for btnBlack
    public void handlePaletteBlack(View v)
    {
        model.changeColor(Color.BLACK);
        checkPaletteButtons();
    }

    // onClick event for btnRed
    public void handlePaletteRed(View v)
    {
        model.changeColor(Color.RED);
        checkPaletteButtons();
    }

    // onClick event for btnYellow
    public void handlePaletteYellow(View v)
    {
        model.changeColor(Color.YELLOW);
        checkPaletteButtons();
    }

    // onClick event for btnBlue
    public void handlePaletteBlue(View v)
    {
        model.changeColor(Color.BLUE);
        checkPaletteButtons();
    }

    // onClick event for btnGreen
    public void handlePaletteGreen(View v)
    {
        model.changeColor(Color.GREEN);
        checkPaletteButtons();
    }

    // onClick event for btnOrange
    public void handlePaletteOrange(View v)
    {
        model.changeColor(Color.parseColor("#ffa600"));
        checkPaletteButtons();
    }

    // onClick event for rdbMinLine
    public void handleMinLine(View v)
    {
        model.changeLineThickness(1);
        linePreview.invalidate();
        checkLineButtons();
    }

    // onClick event for rdbMedLine
    public void handleMedLine(View v)
    {
        model.changeLineThickness(3);
        linePreview.invalidate();
        checkLineButtons();
    }

    // onClick event for rdbMaxLine
    public void handleMaxLine(View v)
    {
        model.changeLineThickness(5);
        linePreview.invalidate();
        checkLineButtons();
    }

    // set tool button toggles
    public void checkToolButtons()
    {
        Tool tool = model.currTool;

        if (tool == Tool.SELECT)
        {
            btnSelect.setChecked(true);
        }
        else
        {
            btnSelect.setChecked(false);
        }

        if (tool == Tool.ERASE)
        {
            btnErase.setChecked(true);
        }
        else
        {
            btnErase.setChecked(false);
        }

        if (tool == Tool.LINE)
        {
            btnLine.setChecked(true);
        }
        else
        {
            btnLine.setChecked(false);
        }

        if (tool == Tool.RECT)
        {
            btnRect.setChecked(true);
        }
        else
        {
            btnRect.setChecked(false);
        }

        if (tool == Tool.OVAL)
        {
            btnOval.setChecked(true);
        }
        else
        {
            btnOval.setChecked(false);
        }

        if (tool == Tool.FILL)
        {
            btnFill.setChecked(true);
        }
        else
        {
            btnFill.setChecked(false);
        }

        canvasView.invalidate();
    }

    // check color palette button toggles
    public void checkPaletteButtons()
    {
        int color = model.currColor;

        if (color == Color.BLACK)
        {
            btnBlack.setChecked(true);
        }
        else
        {
            btnBlack.setChecked(false);
        }

        if (color == Color.RED)
        {
            btnRed.setChecked(true);
        }
        else
        {
            btnRed.setChecked(false);
        }

        if (color == Color.YELLOW)
        {
            btnYellow.setChecked(true);
        }
        else
        {
            btnYellow.setChecked(false);
        }

        if (color == Color.BLUE)
        {
            btnBlue.setChecked(true);
        }
        else
        {
            btnBlue.setChecked(false);
        }

        if (color == Color.GREEN)
        {
            btnGreen.setChecked(true);
        }
        else
        {
            btnGreen.setChecked(false);
        }

        if (color == Color.parseColor("#ffa600"))
        {
            btnOrange.setChecked(true);
        }
        else
        {
            btnOrange.setChecked(false);
        }

        canvasView.invalidate();
    }

    // check radio button toggles based on current line thickness
    public void checkLineButtons()
    {
        int lineThickness = model.lineThickness;

        rdbMinLine.setChecked(lineThickness == 1);
        rdbMedLine.setChecked(lineThickness == 3);
        rdbMaxLine.setChecked(lineThickness == 5);

    }
}
