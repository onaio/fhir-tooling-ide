package org.smartregister.fct.configs.domain.model


/** Represents different types of views that can be rendered via compose */
@Suppress("EXPLICIT_SERIALIZABLE_IS_REQUIRED")
enum class ViewType {
    /** Represent a vertical layout that arranges views one on top of the other */
    COLUMN,

    /** Represents a text view that displays two texts that can be formatted separately */
    COMPOUND_TEXT,

    /** A horizontal layout that arranges views from left to right */
    ROW,

    /** A card like view with an actionable service button used register list rows */
    SERVICE_CARD,

    /** Display a pair of compund texts with the formats label and displayValue */
    PERSONAL_DATA,

    /** Renders a card */
    CARD,

    /** View component used to render a button for click actions */
    BUTTON,

    /** View component used to render a space between views */
    SPACER,

    /** A type of view component used to render items in a list */
    LIST,

    /** A type of view component used to render an icon */
    IMAGE,

    /** A type of view component used to render divider between views */
    BORDER,

    /** A type of view component used to overlay different views */
    STACK,
}
