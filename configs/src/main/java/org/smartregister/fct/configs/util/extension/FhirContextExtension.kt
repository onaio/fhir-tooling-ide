
package org.smartregister.fct.configs.util.extension

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.parser.IParser


fun FhirContext.getCustomJsonParser(): IParser {
  return this.apply {
      registerCustomTypes(
        listOf(
          /*PractitionerDetails::class.java,
          FhirPractitionerDetails::class.java,
          LocationHierarchy::class.java,*/
        ),
      )
    }
    .newJsonParser()
}
