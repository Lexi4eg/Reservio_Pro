using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

public class ApiClient
{
    private static readonly HttpClient client = new HttpClient();

    public async Task SendPostRequestAsync(string url, ReservationRequest reservation)
    {
        try
        {
            string jsonData = JsonSerializer.Serialize(reservation);
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
            
            HttpResponseMessage response = await client.PostAsync(url, content);
            
            if (response.IsSuccessStatusCode)
            {
                Console.WriteLine("POST request successful.");
            }
            else
            {
                Console.WriteLine($"POST request failed. Status: {response.StatusCode}");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error sending POST request: {ex.Message}");
        }
    }
}