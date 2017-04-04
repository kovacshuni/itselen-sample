package com.hunorkovacs.itselen.simple

import io.circe.Codec

case class ItsElenConfig(
    something: Int)
    derives Codec.AsObject
