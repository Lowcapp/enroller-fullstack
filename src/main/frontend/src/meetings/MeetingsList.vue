<template>
  <table v-if="meetings.length > 0">
    <thead>
    <tr>
      <th>Nazwa spotkania</th>
      <th>Opis</th>
      <th>Uczestnicy</th>
      <td></td>
    </tr>
    </thead>
    <tbody>
    <tr v-for="meeting in meetings" :key="meeting.title">
      <td>{{ meeting.title }}</td>
      <td>{{ meeting.description }}</td>
      <td>
        <ul v-if="meeting.participants">
          <li v-for="participant in meeting.participants" :key="participant">
            {{ participant }}
          </li>
        </ul>
      </td>
	  <td style="text-align: right; min-width: 400px">
        <button class="button" @click="$emit('delete', meeting)">
          Usuń puste spotkanie
        </button>
	  </td>
	  
    </tr>
    </tbody>
  </table>
</template>

<script>
    export default {
        props: ['meetings', 'username']
    }
</script>
