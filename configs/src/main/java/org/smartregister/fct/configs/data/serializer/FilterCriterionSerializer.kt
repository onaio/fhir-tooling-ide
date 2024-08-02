package org.smartregister.fct.configs.data.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.commons.lang3.NotImplementedException
import org.hl7.fhir.r4.model.Enumerations.DataType
import org.smartregister.fct.configs.domain.model.FilterCriterionConfig

object FilterCriterionSerializer :
    JsonContentPolymorphicSerializer<FilterCriterionConfig>(FilterCriterionConfig::class) {

    private const val DATA_TYPE = "dataType"

    override fun selectDeserializer(
        element: JsonElement,
    ): DeserializationStrategy<out FilterCriterionConfig> {
        val jsonObject = element.jsonObject
        val dataType = jsonObject[DATA_TYPE]?.jsonPrimitive?.content
        require(dataType != null && DataType.values().contains(DataType.valueOf(dataType))) {
            """`The dataType $dataType` property missing in the JSON .
         Supported types: ${DataType.values()}
         Parsed JSON: $jsonObject
            """
                .trimMargin()
        }
        return when (DataType.valueOf(dataType)) {
            DataType.QUANTITY -> FilterCriterionConfig.QuantityFilterCriterionConfig.serializer()
            DataType.DATETIME,
            DataType.DATE,
            DataType.TIME,
            -> FilterCriterionConfig.DateFilterCriterionConfig.serializer()

            DataType.DECIMAL,
            DataType.INTEGER,
            -> FilterCriterionConfig.NumberFilterCriterionConfig.serializer()

            DataType.STRING -> FilterCriterionConfig.StringFilterCriterionConfig.serializer()
            DataType.URI,
            DataType.URL,
            -> FilterCriterionConfig.UriFilterCriterionConfig.serializer()

            DataType.REFERENCE -> FilterCriterionConfig.ReferenceFilterCriterionConfig.serializer()
            DataType.CODING,
            DataType.CODEABLECONCEPT,
            DataType.CODE,
            -> FilterCriterionConfig.TokenFilterCriterionConfig.serializer()

            else -> {
                throw NotImplementedException("Data type `$dataType` is not supported for data filtering ")
            }
        }
    }
}
