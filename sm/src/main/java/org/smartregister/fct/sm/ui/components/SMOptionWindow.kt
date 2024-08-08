package org.smartregister.fct.sm.ui.components

import UploadFromInputFieldButtonWithDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Input
import androidx.compose.material.icons.outlined.Input
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Resource
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.engine.util.encodeResourceToString
import org.smartregister.fct.logcat.FCTLogger
import org.smartregister.fct.radiance.ui.components.ButtonType
import org.smartregister.fct.radiance.ui.components.OutlinedButton
import org.smartregister.fct.radiance.ui.components.TextButton
import org.smartregister.fct.sm.data.provider.SMTabViewModelProvider
import org.smartregister.fct.sm.data.transformation.SMTransformService
import org.smartregister.fct.sm.data.viewmodel.SMTabViewModel

@Composable
fun SMOptionWindow() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val smTransformService = getKoin().get<SMTransformService>()
        val scope = rememberCoroutineScope()

        Box(
            Modifier.fillMaxWidth().height(40.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Structure Map",
                style = MaterialTheme.typography.titleSmall
            )
            VerticalDivider(modifier = Modifier.align(Alignment.CenterEnd))
            HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Transform",
                    onClick = {

                        getSmTabViewModel()?.let { smTabViewModel ->
                            scope.launch(Dispatchers.IO) {

                                try {
                                    val bundle = smTransformService.transform(
                                        smTabViewModel.codeController.getText(),
                                        questionnaireResponse
                                    )
                                    bundle.entry.forEach {
                                        println(it.resource.encodeResourceToString())
                                    }
                                } catch (t: Throwable) {
                                    println(t.toString())
                                    FCTLogger.e(t)
                                }
                            }
                        }

                    }
                )

                var inputResourceName by remember { mutableStateOf(getSmTabViewModel()?.inputResource?.resourceType?.name ?: "No Input") }

                UploadFromInputFieldButtonWithDialog(
                    modifier = Modifier.fillMaxWidth(),
                    label = inputResourceName,
                    icon = Icons.AutoMirrored.Outlined.Input,
                    buttonType = ButtonType.OutlineButton,
                    codeStyle = CodeStyle.Json,
                    onResult = { uploadResult ->

                        scope.launch(Dispatchers.IO) {
                            try {
                                val resource = uploadResult.decodeResourceFromString<Resource>()
                                getSmTabViewModel()?.inputResource = resource
                                inputResourceName = resource.resourceType.name
                            } catch (t: Throwable) {
                                FCTLogger.e(t)
                            }
                        }
                    }
                )
            }

            SMUploadButton(
                modifier = Modifier.fillMaxWidth(),
                label = "Upload Map",
                icon = null,
                buttonType = ButtonType.OutlineButton,
            )
        }
    }
}

private fun getSmTabViewModel() : SMTabViewModel? {
    val smTabViewModelProvider = getKoin().get<SMTabViewModelProvider>()
    val smTabViewModel = smTabViewModelProvider.getActiveSMTabViewModel()

    if (smTabViewModel == null) {
        FCTLogger.w("smTabViewModel is null")
    }

    return smTabViewModel
}

const val questionnaireResponse = """
    {
  "resourceType": "QuestionnaireResponse",
  "item": [
    {
      "linkId": "85e8d19c-56fe-4af7-961a-5341f449f96e",
      "text": "Caregiver Information"
    },
    {
      "linkId": "0d77d40f-098b-4329-9199-33b6e77aa864",
      "text": "Client ID",
      "answer": [
        {
          "valueInteger": 750143
        }
      ]
    },
    {
      "linkId": "is-edit-profile",
      "answer": [
        {
            "valueBoolean": true
        }
      ]
    },
    {
      "linkId": "9d017fa4-56d3-4d9b-b5f6-f9b3ba7d17a3",
      "text": "First Name",
      "answer": [
        {
          "valueString": "Nancy"
        }
      ]
    },
    {
      "linkId": "7b41d922-376c-49bf-f6a8-faaa681e9ef6",
      "text": "Middle Name"
    },
    {
      "linkId": "76bad83a-f061-4b22-8b4f-a95cbd5be4da",
      "text": "Last Name",
      "answer": [
        {
          "valueString": "Ajram"
        }
      ]
    },
    {
      "linkId": "e79d201a-4e30-40ca-c1ef-060a3d449303"
    },
    {
      "linkId": "aa1ddb98-87a4-48a6-9d8c-4c80de1ec277",
      "text": "Date of Birth",
      "answer": [
        {
          "valueDate": "1996-07-17"
        }
      ]
    },
    {
      "linkId": "calculated-month",
      "text": "Calculated Month (Hidden)",
      "answer": [
        {
          "valueInteger": 7
        }
      ]
    },
    {
      "linkId": "calculated-age",
      "text": "Calculated Age (Hidden)",
      "answer": [
        {
          "valueInteger": 27
        }
      ]
    },
    {
      "linkId": "age",
      "text": "Age (Hidden)",
      "answer": [
        {
          "valueInteger": 27
        }
      ]
    },
    {
      "linkId": "50330b11-c520-4f45-a8f4-44771097788d",
      "text": "Sex",
      "answer": [
        {
          "valueCoding": {
            "system": "http://hl7.org/fhir/administrative-gender",
            "code": "female",
            "display": "Female"
          }
        }
      ]
    },
    {
      "linkId": "location-widget",
      "text": "Record GPS Location",
      "item": [
        {
          "linkId": "latitude",
          "text": "Latitude",
          "answer": [
            {
              "valueDecimal": 27.2323
            }
          ]
        },
        {
          "linkId": "longitude",
          "text": "Longitude",
          "answer": [
            {
              "valueDecimal": 24.232339322
            }
          ]
        }
      ]
    },
    {
      "linkId": "59d21749-f4d7-4725-bec5-e924d3ce1eeb",
      "text": "Select Identification",
      "answer": [
        {
          "valueCoding": {
            "system": "https://fhir.demo.smartregister.org/fhir/national-id-number",
            "code": "national-id-number",
            "display": "National ID Number"
          }
        }
      ]
    },
    {
      "linkId": "8d420cab-a72b-4190-ff05-306135edb39e",
      "text": "Identification Number",
      "answer": [
        {
          "valueString": "17362626"
        }
      ]
    },
    {
      "linkId": "30667e76-8f1c-452c-cecd-fb99dfea485c",
      "text": "Vaccination Card Number",
      "answer": [
        {
          "valueInteger": 581675
        }
      ]
    },
    {
      "linkId": "857e19ee-4b60-4e03-bb0f-d68e872edc78",
      "text": "Occupation",
      "answer": [
        {
          "valueCoding": {
            "id": "be13d4ee-3bbb-49f4-af9d-bce32bf7b6d3",
            "system": "urn:uuid:cf6c0053-e6f6-4687-89e2-75932fa35481",
            "code": "teacher",
            "display": "Teacher"
          }
        }
      ]
    },
    {
      "linkId": "3f3ab03a-7a4a-11ee-b962-0242ac120002",
      "text": "Province"
    },
    {
      "linkId": "ab2161d1-b4c3-47a1-a4b9-0c0b916842f4",
      "text": "District"
    },
    {
      "linkId": "b00bda77-6df2-4660-8134-c424fb1dbf4f",
      "text": "Village"
    },
    {
      "linkId": "85e8d19c-56fe-4af7-961a-5341f449f9-6e",
      "text": "Child Information"
    },
    {
      "linkId": "a23b1abb-6c10-487c-94c4-43318da81e5f",
      "text": "Name",
      "answer": [
        {
          "valueString": "Shami"
        }
      ]
    },
    {
      "linkId": "ffd72b7a-473a-43bc-80ed-449224e5f216",
      "text": "Date of birth",
      "answer": [
        {
          "valueDate": "2023-11-15"
        }
      ]
    },
    {
      "linkId": "e02f9f90-f21f-49c7-bff8-7a4e598fd2bd"
    },
    {
      "linkId": "a128c2e1-3273-40e6-909c-91944232b061",
      "text": "Sex",
      "answer": [
        {
          "valueCoding": {
            "system": "http://hl7.org/fhir/administrative-gender",
            "code": "male",
            "display": "Male"
          }
        }
      ]
    },
    {
      "linkId": "b33a4361-e9ff-49dd-afbd-b80c91a119ea",
      "text": "Birth weight (kg)",
      "answer": [
        {
          "valueInteger": 2
        }
      ]
    },
    {
      "linkId": "21922181-ec78-481d-97f9-9d91a2b31e1f",
      "text": "Birth height (cm)",
      "answer": [
        {
          "valueInteger": 25
        }
      ]
    },
    {
      "linkId": "caregiver-present",
      "answer": [
        {
          "valueBoolean": false
        }
      ]
    },
    {
      "linkId": "caregiver-id",
      "answer": [
        {
          "valueString": ""
        }
      ]
    },
    {
      "linkId": "location-id",
      "answer": [
        {
          "valueString": ""
        }
      ]
    },


    {
      "linkId": "patient-id",
      "text": "Patient's Id",
      "answer": [
        {
          "valueString": "sfd"
        }
      ]
    },
    {
      "linkId": "patient-age",
      "text": "Patient's Age",
      "answer": [
        {
          "valueInteger": 0
        }
      ]
    },
    {
      "linkId": "child-vaccines",
      "text": "Vaccine that caused the reaction",
      "answer": [
        {
          "valueReference": {
            "reference": "Immunization/44a42884-9a14-4168-bac1-848a122f8c23",
            "display": "BCG 0"
          }
        }
      ]
    },
    {
      "linkId": "recorded-date",
      "text": "Date that the patient started experiencing the reaction",
      "answer": [
        {
          "valueDate": "2024-07-09"
        }
      ]
    },
    {
      "linkId": "a2f71c64-5ccc-4575-8638-d51aeefe6e6c",
      "text": "Reaction manifestation",
      "answer": [
        {
          "valueCoding": {
            "id": "f9e4cbe6-e0a3-4abb-804d-19528f1ac919",
            "system": "urn:uuid:65f1c0a0-5fcc-409c-8bd0-097fe9458040",
            "code": "seizures",
            "display": "Seizures"
          }
        }
      ]
    },
    {
      "linkId": "287f1e51-937b-4a44-ab72-6b82a0a1475f",
      "text": "Type of reaction",
      "answer": [
        {
          "valueCoding": {
            "id": "89051954-8a41-4789-ee4a-a325946a1975",
            "system": "urn:uuid:0b28611c-7e8f-42b9-fba2-968f1e4bf523",
            "code": "life-threatening",
            "display": "Life threatening"
          }
        }
      ]
    },
    {
      "linkId": "9bbf6a69-3c16-4052-82a5-8ca9f9102ef3",
      "text": "Reaction outcome",
      "answer": [
        {
          "valueCoding": {
            "id": "7446e2fc-78d8-47e1-d73f-1d15ade5c22f",
            "system": "urn:uuid:2aae15e8-4873-4460-e111-470f2d5b3a40",
            "code": "died",
            "display": "Died"
          }
        }
      ]
    },
    {
      "linkId": "670552cf-cd9e-4596-9d89-5d6e4872d523",
      "text": "Date of Death",
      "answer": [
        {
          "valueDate": "2024-07-09"
        }
      ]
    },
    {
      "linkId": "referred",
      "text": "Was the patient referred?",
      "answer": [
        {
          "valueCoding": {
            "id": "d9dce381-acaf-4098-e17b-669707b17ead",
            "system": "urn:uuid:220f33dc-4fc6-4a6b-c16f-006ea550cf52",
            "code": "no",
            "display": "No"
          }
        }
      ]
    },
    {
      "linkId": "aefi-form-completed",
      "text": "AEFI form completed?",
      "answer": [
        {
          "valueCoding": {
            "id": "37c938fb-3bc7-437b-c921-615f77a6494e",
            "system": "urn:uuid:d63e420a-b83d-4954-d1a2-a46497760fa8",
            "code": "no",
            "display": "No"
          }
        }
      ]
    }
  ]
}
"""