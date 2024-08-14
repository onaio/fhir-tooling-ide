package org.smartregister.fct.fm.data

import org.smartregister.fct.fm.domain.handler.InAppFileHandler
import org.smartregister.fct.fm.domain.handler.SystemFileHandler

internal class FileHandler(val systemFileHandler: SystemFileHandler, val inAppFileHandler: InAppFileHandler)