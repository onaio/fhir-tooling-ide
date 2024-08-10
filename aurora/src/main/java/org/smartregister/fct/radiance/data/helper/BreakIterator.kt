package org.smartregister.fct.radiance.data.helper

interface BreakIterator {
  fun next(): Int

  fun makeCharacterInstance(): BreakIterator

  fun setText(text: String?)

  fun current(): Int

  companion object {
    const val DONE = -1
  }
}