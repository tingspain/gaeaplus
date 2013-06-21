/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package gov.nasa.worldwind.ogc.kml;

import gov.nasa.worldwind.event.Message;
import gov.nasa.worldwind.util.Logging;

import java.util.Map;

/**
 * Represents the KML <i>Style</i> element and provides access to its contents.
 *
 * @author tag
 * @version $Id: KMLStyle.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class KMLStyle extends KMLAbstractStyleSelector
{
    /**
     * Construct an instance.
     *
     * @param namespaceURI the qualifying namespace URI. May be null to indicate no namespace qualification.
     */
    public KMLStyle(String namespaceURI)
    {
        super(namespaceURI);
    }

    //X-PATCH Marjan
    //added clone constructor
    public KMLStyle(KMLStyle orig)
    {
        super(orig);
        setField(KMLConstants.ICON_STYLE_FIELD, orig.getIconStyle() == null
                ? null : new KMLIconStyle(orig.getIconStyle()));
        setField(KMLConstants.LABEL_STYLE_FIELD, orig.getLabelStyle() == null
                ? null : new KMLLabelStyle(orig.getLabelStyle()));
        setField(KMLConstants.LINE_STYLE_FIELD, orig.getLineStyle() == null
                ? null : new KMLLineStyle(orig.getLineStyle()));
        setField(KMLConstants.POLY_STYLE_FIELD, orig.getPolyStyle() == null
                ? null : new KMLPolyStyle(orig.getPolyStyle()));
        setField(KMLConstants.BALOON_STYLE_FIELD, orig.getBaloonStyle() == null
                ? null : new KMLBalloonStyle(orig.getBaloonStyle()));
        setField(KMLConstants.LIST_STYLE_FIELD, orig.getListStyle() == null
                ? null : new KMLListStyle(orig.getListStyle()));
    }
    //X-END
    
    public KMLIconStyle getIconStyle()
    {
        return (KMLIconStyle) this.getField(KMLConstants.ICON_STYLE_FIELD);
    }

    public KMLLabelStyle getLabelStyle()
    {
        return (KMLLabelStyle) this.getField(KMLConstants.LABEL_STYLE_FIELD);
    }

    public KMLLineStyle getLineStyle()
    {
        return (KMLLineStyle) this.getField(KMLConstants.LINE_STYLE_FIELD);
    }

    public KMLPolyStyle getPolyStyle()
    {
        return (KMLPolyStyle) this.getField(KMLConstants.POLY_STYLE_FIELD);
    }

    public KMLBalloonStyle getBaloonStyle()
    {
        return (KMLBalloonStyle) this.getField(KMLConstants.BALOON_STYLE_FIELD);
    }

    public KMLListStyle getListStyle()
    {
        return (KMLListStyle) this.getField(KMLConstants.LIST_STYLE_FIELD);
    }

    /**
     * Adds the sub-style fields of a specified sub-style to this one's fields if they don't already exist.
     *
     * @param subStyle the sub-style to merge with this one.
     *
     * @return the substyle passed in as the parameter.
     *
     * @throws IllegalArgumentException if the sub-style parameter is null.
     */
    public KMLAbstractSubStyle mergeSubStyle(KMLAbstractSubStyle subStyle)
    {
        if (subStyle == null)
        {
            String message = Logging.getMessage("nullValue.SymbolIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (!this.hasFields())
            return subStyle;

        Class subStyleClass = subStyle.getClass();
        for (Map.Entry<String, Object> field : this.getFields().getEntries())
        {
            if (field.getValue() != null && field.getValue().getClass().equals(subStyleClass))
            {
                this.overrideFields(subStyle, (KMLAbstractSubStyle) field.getValue());
            }
        }

        return subStyle;
    }

    @Override
    public void applyChange(KMLAbstractObject sourceValues)
    {
        if (!(sourceValues instanceof KMLStyle))
        {
            String message = Logging.getMessage("KML.InvalidElementType", sourceValues.getClass().getName());
            Logging.logger().warning(message);
            throw new IllegalArgumentException(message);
        }

        super.applyChange(sourceValues);

        this.onChange(new Message(KMLAbstractObject.MSG_STYLE_CHANGED, this));
    }
}