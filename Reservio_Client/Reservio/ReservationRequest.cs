using System;

public class ReservationRequest
{
    public string id { get; set; }
    public string firstname { get; set; }
    public string lastname { get; set; }
    public DateTime date { get; set; } 
    public int peopleCount { get; set; }
    public string email { get; set; }
    public string phoneNumber { get; set; }
    public string specialRequests { get; set; }
    public bool highChair { get; set; }
    public string tableID { get; set; }
    public int? numberChairs { get; set; } 
}